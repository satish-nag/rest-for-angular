package com.test.spring;


import java.util.Arrays;

public class TestClass {
    public static void main(String[] args){
        char[] sentence = "today is good day".toCharArray();
        System.out.println(reverseSentenceWords(sentence,0).trim());
        System.out.println("=================================");
        System.out.println(reverseSentence(sentence,0));
        System.out.println("=================================");
        System.out.println(reverseSentenceWithoutRecurssion(sentence));
        System.out.println("=================================");
        System.out.println(testPalindrome("Red rum sir is murder".replaceAll(" ","")
                .toLowerCase().toCharArray()));
        System.out.println("=================================");
        checkFrequencyOfChars("Red rum sir is murder".replaceAll(" ",""));
    }

    //O(nlogn)+O(n)
    private static void checkFrequencyOfChars(String s) {
        char[] sortedSentence = mergeSort(s.toCharArray(),0,s.length());
        int i=1;
        char tmp=sortedSentence[0];
        for(int j=1;j<sortedSentence.length;j++){
            if(tmp==sortedSentence[j]) i++;
            else {
                System.out.print("number of occurences of "+tmp+":"+i);
                i=1;
                tmp=sortedSentence[j];
                System.out.println();
            }
        }
        System.out.print("number of occurences of "+tmp+":"+i);
    }

    private static String reverseSentenceWords(char[] sentence, int i) {
        StringBuffer sb=new StringBuffer();
        while(i<sentence.length && sentence[i]!=' '){
            sb.append(sentence[i++]);
        }
        if(i>sentence.length) return sb.toString();
        return reverseSentenceWords(sentence,i+1)+" "+sb.toString();
    }

    private static String reverseSentence(char[] sentence, int i){
        if(i>=sentence.length-1) return sentence[i]+"";
        return reverseSentence(sentence,i+1)+sentence[i];
    }

    // O(n/2)
    private static String reverseSentenceWithoutRecurssion(char[] sentence){
        for(int i=0;i<sentence.length>>1;i++){
            char tmp = sentence[i];
            sentence[i]=sentence[sentence.length-i-1];
            sentence[sentence.length-i-1] = tmp;
        }
        return new String(sentence);
    }

    //O(n/2)
    private static boolean testPalindrome(char[] sentence){
        for(int i=0;i<sentence.length>>1;i++){
            if(sentence[i]!=sentence[sentence.length-i-1])
                return false;
        }
        return true;
    }

    //O(nlogn)
    private static char[] mergeSort(char[] sentence,int begin,int end){
        if(end-begin==1)
            return new char[]{sentence[begin]};
        char[] leftSorted = mergeSort(sentence,begin,(begin+end)>>1);
        char[] rightSorted = mergeSort(sentence,(begin+end)>>1,end);
        char[] sortedSentence=new char[end-begin];
        int i=0,j=0,k=0;
        for(;i<leftSorted.length && j<rightSorted.length;){
            if(leftSorted[i]>rightSorted[j])
                sortedSentence[k++]=rightSorted[j++];
            else
                sortedSentence[k++]=leftSorted[i++];
        }

        while(i<leftSorted.length)
            sortedSentence[k++] = leftSorted[i++];
        while(j<rightSorted.length)
            sortedSentence[k++] = rightSorted[j++];
        return sortedSentence;
    }
}

