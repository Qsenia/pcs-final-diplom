import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class BooleanSearchEngine implements SearchEngine {
    protected Map<String, List<PageEntry>> listMap = new HashMap<>();

    public void setListMap(Map<String, List<PageEntry>> listMap) {
        this.listMap = listMap;
    }

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        // прочтите тут все pdf и сохраните нужные данные,
        for (File pdf : Objects.requireNonNull(pdfsDir.listFiles())) {
            //сканируем каждый пдффайл
            var doc = new PdfDocument(new PdfReader(pdf));
            var number = doc.getNumberOfPages();
            for (int i = 1; i <= number; i++) {
                //перебираем страницы
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
                    //подсчитываем количество слов
                }
                for (var word : freqs.keySet()) {
                    var pdfName = pdf.getName();
                    PageEntry pageEntry = new PageEntry(pdfName, i, freqs.get(word));
                    //для каждого уникального слова создаем объект
                    if (!listMap.containsKey(word)) {
                        listMap.put(word, new ArrayList<>());
                        //при добавлении слова инициализируем список
                        listMap.get(word).add(pageEntry);
                    } else {
                        listMap.get(word).add(pageEntry);
                    }

                }
                listMap.values().forEach(Collections::sort);//сделала сортировку
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        List<PageEntry> listEntries = new ArrayList<>();
        if (listMap.containsKey(word.toLowerCase())) {
            listEntries.addAll(listMap.get(word.toLowerCase()));
        }
        return listEntries;
    }
}

