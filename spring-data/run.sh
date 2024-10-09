#!/bin/sh

mvn compile exec:java -Dexec.mainClass="com.epam.homework4.App" -Dspring-boot.run.profiles=dev
