package net.devbase.jfreesteel.viewer;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.*;
import net.devbase.jfreesteel.EidInfo;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class FormPdfReport {
    java.awt.Image photo;
    EidInfo info;
    public static final String SRC = "izjava2.pdf";

    public FormPdfReport(EidInfo info, java.awt.Image photo) {
        this.info = info;
        this.photo = photo;
    }

    public void write(final String filename) throws IOException, DocumentException {
        PdfReader pdfReader = new PdfReader(SRC);
        PdfStamper stamper = new PdfStamper(pdfReader, new FileOutputStream(filename));
        fillOutForm(stamper);
        stamper.close();
    }

    public ByteArrayOutputStream createPDF() throws DocumentException, IOException {
        ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
        PdfReader reader = new PdfReader(SRC);
        PdfStamper stamper = new PdfStamper(reader, baosPDF);
        fillOutForm(stamper);
        stamper.close();
        reader.close();
        return baosPDF;
    }

    private void fillOutForm(PdfStamper stamper) throws DocumentException, IOException {
        AcroFields form = stamper.getAcroFields();
        form.setField("izborna_lista", "Староседеоци Метла 2020 - Иван Матовић");
        form.setField("kandidat", "Иван Матовић");
        form.setField("prezime_i_ime", info.getSurname() + " " + info.getGivenName());
        form.setField("adresa", info.getAddress(null, null, null));
        form.setField("jmbg", info.getPersonalNumber());
        form.setField("ime_i_prezime_2", info.getNameFull());
        form.setField("datum_rodjenja", info.getDateOfBirth());
        form.setField("adresa_2", info.getPlaceFull("улаз %s", "%s. спрат", "бр. %s"));
        form.setField("dokument", "лична карта");

        String documentInfo = String.format(
                    "%s, %s, %s, %s", "лична карта", info.getDocRegNo(), info.getIssuingDate(), info.getIssuingAuthority());
        form.setField("info_o_dokumentu", documentInfo);
    }

    private void writeDocument(Document doc, PdfWriter writer) throws DocumentException, IOException {

        // Write image: Read from byte stream, as otherwise photo gets wash out
//        ByteArrayOutputStream bas = new ByteArrayOutputStream();
//        ImageIO.write((BufferedImage) photo, "jpeg", bas);
//        byte[] data = bas.toByteArray();
//        Image image = Image.getInstance(data);
//        image.setAbsolutePosition(60, 572);
//        image.setBorder(Image.BOX);
//        image.setBorderWidth(1f);
//        image.scaleAbsolute(119, 158);
        //writer.getDirectContent().addImage(image);

        // Write info
        PdfContentByte cb = writer.getDirectContent();

//        drawRulers(cb, 2f, 782, 747);
//        drawRulers(cb, 1.5f, 554, 529, 304, 279);
        drawRulers(cb, 1f, 640, 590);
        drawVerticalLine(cb, 1f, 59, 590, 640);
        drawVerticalLine(cb, 1f, 536, 590, 640);


        drawRulers(cb, 1f, 510, 460);
        drawVerticalLine(cb, 1f, 59, 510, 460);
        drawVerticalLine(cb, 1f, 536, 510, 460);

        String fontPath = getClass().getResource("/net/devbase/jfreesteel/viewer/arial.ttf").toString();
        BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        String italicFontPath = getClass().getResource("/net/devbase/jfreesteel/viewer/arial.ttf").toString();
        BaseFont italicBf = BaseFont.createFont(italicFontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        String boldFontPath = getClass().getResource("/net/devbase/jfreesteel/viewer/arialBoldFont.ttf").toString();
        BaseFont boldBf = BaseFont.createFont(boldFontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        drawHorizontalLine(cb, 1f, 300, 536, 380);
        drawHorizontalLine(cb, 1f, 300, 536, 340);
        //drawHorizontalLine(cb, 1f, 300, 536, 300);
        drawHorizontalLine(cb, 1f, 300, 536, 260);
        drawHorizontalLine(cb, 1f, 300, 536, 220);

        drawJmbgBox(cb, 300, 300);

        cb.beginText();

        cb.setFontAndSize(boldBf, 15);
        writeText(cb, "И З Ј А В А", 260, 760);
        writeText(cb, "БИРАЧА ДА ПОДРЖАВА ИЗБОРНУ ЛИСТУ КАНДИДАТА", 90, 740);
        writeText(cb, "ЗА ОДБОРНИКЕ СКУПШТИНЕ ГРАДА КРАЉЕВА", 120, 720);

        cb.setFontAndSize(boldBf, 13);
        writeText(cb, "ПОДРЖАВАМ ИЗБОРНУ ЛИСТУ", 190, 660);

        cb.setFontAndSize(italicBf, 10);
        writeText(cb, "(назив изборне листе и име презиме носиоца изборне листе)", 150, 575);

        cb.setFontAndSize(boldBf, 13);
        writeText(cb, "коју за изборе за одборнике Скупштине града Краљева, расписане за", 70, 550);
        writeText(cb, "24 април 2016. године, Изборној комисији града Краљева подноси", 80, 535);

        cb.setFontAndSize(italicBf, 10);
        writeText(cb, "(назив подносиоца изборне листе)", 215, 445);

        cb.setFontAndSize(boldBf, 10);
        int biracTextWidth = (int) boldBf.getWidthPoint("БИРАЧ", 10);
        int biracStartPosition = 418 - biracTextWidth / 2;
        writeText(cb, "БИРАЧ", biracStartPosition, 410);

        cb.setFontAndSize(boldBf, 8);
        int potpisTextWidth = (int) boldBf.getWidthPoint("(потпис)", 8);
        int potpisStartPosition = 418 - potpisTextWidth / 2;
        writeText(cb, "(потпис)", potpisStartPosition, 370);

        cb.setFontAndSize(boldBf, 8);
        int piiTextWidth = (int) boldBf.getWidthPoint("(презиме и име)", 8);
        int piiStartPosition = 418 - piiTextWidth / 2;
        writeText(cb, "(презиме и име)", piiStartPosition, 330);

        cb.setFontAndSize(boldBf, 8);
        int jmbgTextWidth = (int) boldBf.getWidthPoint("(ЈМБГ)", 8);
        int jmbgStartPosition = 418 - jmbgTextWidth / 2;
        writeText(cb, "(ЈМБГ)", jmbgStartPosition, 290);

        cb.setFontAndSize(boldBf, 8);
        int piasTextWidth = (int) boldBf.getWidthPoint("(пребивалиште и адреса становања)", 8);
        int piasStartPosition = 418 - piasTextWidth / 2;
        writeText(cb, "(пребивалиште и адреса становања)", piasStartPosition, 210);

        //writeText(cb, "ЧИТАЧ  ЕЛЕКТРОНСКЕ  ЛИЧНЕ  КАРТЕ: ШТАМПА  ПОДАТАКА", 62, 760);

//        cb.setFontAndSize(bf, 11);
//        writeLabel(cb, "Подаци о грађанину", 537);
//        writeLabel(cb, "Подаци о документу", 288);
//
//        cb.setFontAndSize(bf, 10);
//        writeLine(cb, "Презиме:", info.getSurname(), 513);
//        writeLine(cb, "Име:", info.getGivenName(), 489);
//        writeLine(cb, "Име једног родитеља:", info.getParentGivenName(), 463);
//        writeLine(cb, "Датум рођења:", info.getDateOfBirth(), 440);
//        writeLabel(cb, "Место рођења,", 415);
//        writeLabel(cb, "општина и држава:", 403);
//        writeLine(cb, "", info.getPlaceOfBirthFull().replace("\n", ", "), 409);
//        writeLabel(cb, "Пребивалиште и", 380);
//        writeLabel(cb, "адреса стана:", 368);
//        writePlace(cb, info);
//        writeLine(cb, "ЈМБГ:", info.getPersonalNumber(), 340);
//        writeLine(cb, "Пол:", info.getSex(), 316);
//
//        writeLine(cb, "Документ издаје:", info.getIssuingAuthority(), 262);
//        writeLine(cb, "Број документа:", info.getDocRegNo(), 238);
//        writeLine(cb, "Датум издавања:", info.getIssuingDate(), 215);
//        writeLine(cb, "Важи до:", info.getExpiryDate(), 190);

        cb.endText();
    }

    private void drawJmbgBox(PdfContentByte cb, int startX, int startY) {

        for (int i = 0; i < 13; i++) {
            int x = startX + i * 18;
            int y = startY;
            cb.moveTo(x, y);
            cb.lineTo(x, y + 18);
            cb.lineTo(x + 18, y + 18);
            cb.lineTo(x + 18, y);
            cb.lineTo(x, y);
            cb.stroke();
        }
    }

    private void writePlace(PdfContentByte cb, EidInfo info) throws DocumentException, IOException {

        String place[] = info.getPlaceFull("/ %s", "%s. sprat", "stan %s").split("\n");

        if (place.length > 1) {
            for (int i = 2; i < place.length; i++)
                place[1] += ", " + place[i];

            writeLine(cb, "", place[0], 380);
            writeLine(cb, "", place[1], 368);
        } else {
            writeLine(cb, "", place[0], 374);
        }
    }

    private void drawRulerLine(PdfContentByte cb, int height) {
        cb.moveTo(59, height);
        cb.lineTo(536, height);
        cb.stroke();
    }

    private void drawVerticalLine(PdfContentByte cb, float weight, int start, int height1, int height2) {
        cb.moveTo(start, height1);
        cb.lineTo(start, height2);
        cb.stroke();
    }

    private void drawHorizontalLine(PdfContentByte cb, float weight, int start, int end, int heigh) {
        cb.moveTo(start, heigh);
        cb.lineTo(end, heigh);
        cb.stroke();
    }

    private void drawRulers(PdfContentByte cb, float weight, int... heights) {
        cb.setLineWidth(weight);
        for (int height : heights)
            drawRulerLine(cb, height);
    }

    /**
     * Write text into PdfContentByte
     *
     * @param cb     PDF Content
     * @param text   String to be drawn
     * @param height Y Position
     * @throws DocumentException
     * @throws IOException
     */
    private void writeText(PdfContentByte cb, String text, int x, int y) throws DocumentException, IOException {
        cb.setTextMatrix(x, y);
        cb.showText(text);
    }

    private void writeLabel(PdfContentByte cb, String text, int height) throws DocumentException, IOException {
        writeText(cb, text, 68, height);
    }

    private void writeLine(PdfContentByte cb, String label, String text, int height) throws DocumentException, IOException {
        writeLabel(cb, label, height);
        writeText(cb, text, 200, height);
    }
}
