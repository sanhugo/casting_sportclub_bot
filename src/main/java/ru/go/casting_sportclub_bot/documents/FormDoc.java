package ru.go.casting_sportclub_bot.documents;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import ru.go.casting_sportclub_bot.model.Usercard;
import ru.go.casting_sportclub_bot.repository.UserCardRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class FormDoc {
    UserCardRepository usercardRepository;

    public FormDoc(UserCardRepository usercardRepository) {
        this.usercardRepository = usercardRepository;
    }

    public File exportAllToExcel() throws IOException {
        List<Usercard> users = usercardRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Usercards");

        // Заголовки
        String[] headers = {
                "Факультет", "Телефон", "Возраст", "Имя",
                "Фамилия", "Курс", "Выбранные направления", "Опыт организации",
                "Опыт участия", "Дата регистрации"
        };

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Данные
        int rowIdx = 1;
        for (Usercard u : users) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(u.getTgid());
            row.createCell(0).setCellValue(u.getFaculty() != null ? u.getFaculty().name() : "");
            row.createCell(1).setCellValue(u.getPhone());
            row.createCell(2).setCellValue(u.getAge());
            row.createCell(3).setCellValue(u.getName());
            row.createCell(4).setCellValue(u.getSurname());
            row.createCell(5).setCellValue(u.getCourse());
            row.createCell(6).setCellValue(
                    u.getChoices() != null
                            ? u.getChoices().stream()
                            .map(Enum::name)
                            .collect(Collectors.joining(", "))
                            : ""
            );
            row.createCell(7).setCellValue(u.getEventmaking());
            row.createCell(8).setCellValue(u.getEventpart());
            row.createCell(9).setCellValue(
                    u.getRegdate() != null ? u.getRegdate().format(dateFormatter) : ""
            );
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Сохраняем во временный файл
        File file = File.createTempFile("usercards_", ".xlsx");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
        workbook.close();
        return file;
    }
}
