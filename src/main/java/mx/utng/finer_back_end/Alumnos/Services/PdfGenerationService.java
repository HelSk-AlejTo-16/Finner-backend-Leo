package mx.utng.finer_back_end.Alumnos.Services;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.element.Table;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;

import mx.utng.finer_back_end.Alumnos.Documentos.CertificadoDetalleDTO;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PdfGenerationService {

    public byte[] generarCertificado(CertificadoDetalleDTO certificadoDetalleDTO) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Crear el escritor y documento PDF con orientación horizontal
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument, PageSize.A4.rotate()); // A4 con orientación horizontal

        // Definir color verde personalizado en RGB
        DeviceRgb customGreen = new DeviceRgb(50, 205, 32); // Verde #32CD32 en RGB

        // Título de la página con color personalizado
        Paragraph titulo = new Paragraph("Certificado de Curso")
                .setFontSize(28)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(customGreen) // Color verde
                .setMarginBottom(20);
        document.add(titulo);

        // Cargar el logo desde recursos
        String logoPath = getClass().getClassLoader().getResource("finer_logo.png").getPath();
        Image logo = new Image(ImageDataFactory.create(logoPath));
        logo.setWidth(100).setHeight(100); // Ajustar el tamaño del logo
        document.add(logo.setFixedPosition(10, 520)); // Coloca el logo en la parte superior izquierda

        // Nombre de la app "Finer"
        Paragraph appName = new Paragraph("Finer")
                .setFontSize(24)
                .setBold()
                .setTextAlignment(TextAlignment.LEFT)
                .setFontColor(customGreen) // Color verde
                .setFixedPosition(120, 520, 300); // Coloca el nombre de la app al lado del logo
        document.add(appName);

        // Detalle de los datos del certificado
        Paragraph detalleAlumno = new Paragraph("Este certificado se otorga a:")
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(detalleAlumno);

        // Nombre del alumno
        Paragraph nombreAlumno = new Paragraph(certificadoDetalleDTO.getNombreCompletoAlumno())
                .setFontSize(22)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(customGreen) // Color verde
                .setMarginBottom(20);
        document.add(nombreAlumno);

        // Explicación adicional
        Paragraph curso = new Paragraph("por completar con éxito")
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(curso);

        // Título del curso
        Paragraph nombreCurso = new Paragraph(certificadoDetalleDTO.getTituloCurso())
                .setFontSize(24)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(customGreen) // Color verde
                .setMarginBottom(40);
        document.add(nombreCurso);

        // Información adicional
        Paragraph detallesAdicionales = new Paragraph(
                "Curso ofrecido por Universidad Tecnológica del Norte de Guanajuato")
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(detallesAdicionales);

        // Firma del instructor
        Paragraph firma = new Paragraph("Firma del Instructor")
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(10);
        document.add(firma);

        document.add(new Paragraph("_____________________")
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph(certificadoDetalleDTO.getNombreInstructor())
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(30));

        // Crear tabla para la información del instructor, matrícula, fecha de
        // inscripción y fecha de generación
        float[] columnWidths = { 1, 2, 1, 2 }; // Cuatro columnas para los datos
        Table infoTable = new Table(columnWidths);

        // Establecer el color de fondo blanco para las filas
        infoTable.setBackgroundColor(DeviceRgb.WHITE);
        infoTable.setBorder(null);

        // Agregar celdas para cada tipo de información
        infoTable.addCell("Instructor:");
        infoTable.addCell(certificadoDetalleDTO.getNombreInstructor());

        infoTable.addCell("Matrícula:");
        infoTable.addCell(certificadoDetalleDTO.getMatricula());

        infoTable.addCell("Fecha de Inscripción:");
        infoTable.addCell(certificadoDetalleDTO.getFechaInscripcion().toString());

        infoTable.addCell("Fecha de Generación:");
        infoTable.addCell(certificadoDetalleDTO.getFechaGeneracion().toString());

        // Agregar la tabla al documento
        // Cambié la posición para que esté más abajo y ocupe todo el margen disponible
        document.add(infoTable.setFixedPosition(50, 50, 750)); // 50 px desde la izquierda, 50 px desde la parte inferior,
                                                             // 500 px de ancho (ocupando todo el margen)

        // Cerrar documento PDF
        document.close();

        return byteArrayOutputStream.toByteArray();
    }
}
