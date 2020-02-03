import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Test {
    private int depth=0;
    private List<String> columnNames=new ArrayList<>();
    private Map<String,Map<Integer,String>> columnData = new HashMap<>();

    private static ObjectMapper objectMapper=new ObjectMapper();

    public static void main(String[] args) throws IOException {
        new Test().convertJsonToCsv(Files.readAllLines(Paths.get("src/main/resources/input.json")).stream().collect(Collectors.joining(System.lineSeparator())));
    }

    public void convertJsonToCsv(String jsonString) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        if(jsonNode.isArray()){
            Iterator<JsonNode> elements = jsonNode.elements();
            while(elements.hasNext()){
                JsonNode next = elements.next();
                convertJsonToCsvUtil(next,depth);
                depth++;
            }
        }
        else if(jsonNode.isObject()){
            convertJsonToCsvUtil(jsonNode,depth);
            depth++;
        }

        System.out.println(columnNames.stream().sorted().collect(Collectors.joining(",")));
        IntStream.range(0, depth).forEach(value -> {
            columnNames.stream().sorted().forEach(columnName -> {
                if(columnData.get(columnName).get(value)!=null)
                    System.out.print(columnData.get(columnName).get(value));
                System.out.print(",");
            });
            System.out.println();
        });
    }

    private void convertJsonToCsvUtil(JsonNode next, int rows) {
        if(next.isObject()){
            Iterator<Map.Entry<String, JsonNode>> fields = next.fields();
            while (fields.hasNext()){
                Map.Entry<String, JsonNode> jsonNodeEntry = fields.next();
                if(jsonNodeEntry.getValue().isValueNode()){
                    if(!columnNames.contains(jsonNodeEntry.getKey())) {
                        columnNames.add(jsonNodeEntry.getKey());
                        columnData.put(jsonNodeEntry.getKey(), new HashMap<>());
                    }
                    columnData.get(jsonNodeEntry.getKey()).put(rows,jsonNodeEntry.getValue().toString());
                }
                else if(jsonNodeEntry.getValue().isArray()){
                    Iterator<JsonNode> elements = jsonNodeEntry.getValue().elements();
                    int rows1 = rows;
                    while (elements.hasNext()){
                        convertJsonToCsvUtil(elements.next(),rows1++);
                        depth =depth <rows1-1?rows1-1:depth;
                    }
                }
                else if(jsonNodeEntry.getValue().isObject()){
                    convertJsonToCsvUtil(jsonNodeEntry.getValue(),rows);
                }
            }
        }else if(next.isArray()){
            Iterator<JsonNode> elements = next.elements();
            while(elements.hasNext()){
                convertJsonToCsvUtil(elements.next(),rows);
            }
        }
    }
}
