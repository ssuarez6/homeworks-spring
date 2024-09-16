package com.epam.homework.model;

public record File(
        Integer id,
        String name,
        String type,
        Long size,
        byte[] content
) {}
