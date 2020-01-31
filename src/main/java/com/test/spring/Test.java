package com.test.spring.jms.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

public class Test {
    static Map<String, List<String>> data = new HashMap<>();
    static int k=0;
    public static void main(String[] args) throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree(new File("src/main/resources/input.json"));
        if(jsonNode.isArray()){
            flatRootArray(jsonNode,"",0);
        }
        else if(jsonNode.isObject()){
            flatObject(jsonNode,"",0);
        }

        writeToFile();

    }

    private static void writeToFile() {
        StringBuffer headers = new StringBuffer();
        Map<Integer,String> body = new HashMap<>();
        data.keySet().stream().sorted().forEach(s -> {
            headers.append(s+",");
            List<String> strings = data.get(s);
            for(int i=0;i<strings.size();i++){
                if(StringUtils.isEmpty(body.get(i))){
                    body.put(i,strings.get(i));
                }else{
                    body.put(i,body.get(i)+","+strings.get(i));
                }
            }
        });
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/output.csv"))){
            bw.write(headers.toString());
            bw.newLine();
            for (String s : body.values()) {
                bw.write(s);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void flatRootArray(JsonNode value, String prefix, int index) {
        Iterator<JsonNode> elements = value.elements();
        while(elements.hasNext()){
            JsonNode next = elements.next();
            if(next.isArray())
                flatRootArray(next,prefix,index);
            else{
                flatObject(next,prefix,index);
            }
            k++;
        }
    }

    public static void flatArray(JsonNode value, String prefix, int index){
        Iterator<JsonNode> elements = value.elements();
        while(elements.hasNext()){
            JsonNode next = elements.next();
            if(next.isArray()) flatArray(next,prefix,index);
            else{
                flatObject(next,prefix,index++);
            }
        }
    }

    public static void flatObject(JsonNode jsonNode, String prefix, int index){
        Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
        while(fields.hasNext()){
            Map.Entry<String, JsonNode> next = fields.next();
            String key = next.getKey();
            JsonNode value = next.getValue();
            key=prefix+"_"+key+"_"+index;
            if(value.isArray()) {
                flatArray(value,key,index);
            }
            else if(value.isObject()) {
                flatObject(value,key,index);
            }
            else if(value.isValueNode()){
                if(data.get(key)==null){
                    List<String> strings = new ArrayList<>();
                    if(k>0) IntStream.range(0,k).forEach(s->strings.add(""));
                    strings.add(k,value.textValue());
                    data.put(key,strings);
                }else {
                    data.get(key).add(value.textValue());
                }
            }
        }
    }
}
