package com.epam.homework.controller;

import com.epam.homework.facade.BookingFacade;
import com.epam.homework.model.Ticket;
import com.epam.homework.model.User;
import com.epam.homework.service.TicketService;
import com.epam.homework.xml.TicketXMLDescriptor;
import com.epam.homework.xml.TicketsXMLDescriptor;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Controller
public class TicketController {
    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);

    @Autowired
    private BookingFacade bookingFacade;


    @Autowired
    private Jaxb2Marshaller jaxb2Marshaller;

    public TicketController(BookingFacade bookingFacade, Jaxb2Marshaller jaxb2Marshaller) {
        this.bookingFacade = bookingFacade;
        this.jaxb2Marshaller = jaxb2Marshaller;
    }

    @GetMapping("/tickets-by-user")
    public ResponseEntity<List<Ticket>> getBookedTicketsByUser(@RequestParam String userId, Model model) {
        var user = bookingFacade.getUserById(Long.parseLong(userId));
        var bookedTickets = bookingFacade.getBookedTickets(user, 10, 1);
        return ResponseEntity.ok(bookedTickets);
    }

    @GetMapping(value = "/tickets", params = "asPdf=true")
    public ResponseEntity<?> downloadTicketsPdf(@RequestParam String userId) {
        var user = bookingFacade.getUserById(Long.parseLong(userId));
        var bookedTickets = bookingFacade.getBookedTickets(user, 100, 1);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=tickets.pdf");
        return new ResponseEntity<>(generateTicketsPDF(bookedTickets, user), headers, HttpStatus.OK);
    }

    @GetMapping("/tickets-by-event")
    public ResponseEntity<List<Ticket>> getBookedTicketsByEvent(@RequestParam String eventId, Model model) {
        var event = bookingFacade.getEventById(Long.parseLong(eventId));
        var bookedTickets = bookingFacade.getBookedTickets(event, 10, 1);
        return ResponseEntity.ok(bookedTickets);
    }

    @PostMapping("/tickets")
    public String uploadTickets(@RequestParam("file")MultipartFile file, Model model) {
        try {
            File tempFile = File.createTempFile("tickets", ".xml");
            file.transferTo(tempFile);
            var loadedTickets = parseXmlTickets(tempFile.getAbsolutePath());
            bookingFacade.loadTickets(loadedTickets);
            model.addAttribute("message", "Tickets uploaded successfully");
        }catch (Exception ex) {
            logger.error("There was an error loading the tickets", ex);
            model.addAttribute("message", "There was an error uploading the tickets. Please try again");
        }
        return "uploadTickets";
    }

    private List<Ticket> parseXmlTickets(String filePath) {
        File xmlFile = new File(filePath);
        try (FileInputStream fis = new FileInputStream(xmlFile)) {
            var ticketsXML = (TicketsXMLDescriptor)jaxb2Marshaller.unmarshal(new StreamSource(fis));
            var tickets = ticketsXML.getTicketsXML().stream().map(txml -> new Ticket(Long.parseLong(txml.getEvent()), Long.parseLong(txml.getUser()), Ticket.Category.valueOf(txml.getCategory()), Integer.parseInt(txml.getPlace())));
            return tickets.toList();
        } catch (IOException ioex) {
            logger.error("Could not read XML file", ioex);
            throw new RuntimeException("There was an error reading the XML file");
        }
    }

    private byte[] generateTicketsPDF(List<Ticket> tickets, User user) {
        var bytesOutputStream = new ByteArrayOutputStream();
        try {
            PdfWriter writer = new PdfWriter(bytesOutputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);
            String initialParagraph = String.format("List of tickets for: %s with email: %s", user.getName(), user.getEmail());
            doc.add(new Paragraph(initialParagraph).setFontSize(15.5f));
            Table t = new Table(4);
            t.addCell(new Cell().add(new Paragraph("Event ID")));
            t.addCell(new Cell().add(new Paragraph("User ID")));
            t.addCell(new Cell().add(new Paragraph("Category")));
            t.addCell(new Cell().add(new Paragraph("Place")));
            tickets.forEach(ticket -> {
                t.addCell(new Cell().add(new Paragraph(ticket.getEventId().toString())));
                t.addCell(new Cell().add(new Paragraph(ticket.getUserId().toString())));
                t.addCell(new Cell().add(new Paragraph(ticket.getCategory().name())));
                t.addCell(new Cell().add(new Paragraph(String.valueOf(ticket.getPlace()))));
            });
            doc.add(t);
            doc.close();
        } catch (Exception ex) {
            logger.error("Error building PDF...", ex);
        }
        return bytesOutputStream.toByteArray();
    }
}
