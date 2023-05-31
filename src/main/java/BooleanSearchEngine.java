import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    private Map<String, List<PageEntry>> listMap = new HashMap<>();
    List<PageEntry> listEntries = new ArrayList<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        // прочтите тут все pdf и сохраните нужные данные,
        for (File pdf : Objects.requireNonNull(pdfsDir.listFiles())) {
            var doc = new PdfDocument(new PdfReader(pdf));
            var number = doc.getNumberOfPages();
            for (int i = 1; i <= number; i++) {
                var text = PdfTextExtractor.getTextFromPage(doc.getPage(i));
                var words = text.split("\\P{IsAlphabetic}+");
                Map<String, Integer> freqs = new HashMap<>();
                // мапа, где ключом будет слово, а значением - частота
                for (var word : words) { // перебираем слова
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                }
                var pdfName = pdf.getName();
                for (var word : freqs.keySet()) {
                    if (freqs.containsKey(pdfName)) {
                        PageEntry pageEntry = new PageEntry(pdfName, i, freqs.get(word));
                        listMap.get(word).add(pageEntry);
                    }
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        Iterator<PageEntry> iterator = listEntries.iterator();
        String lowCase = word.toLowerCase();
        while (iterator.hasNext()) {
            if (listMap.get(lowCase) != null) {
                for (PageEntry pageEntry : listMap.get(lowCase)) {
                    listEntries.add(pageEntry);
                }
            }
        }
        return listEntries;
    }
}