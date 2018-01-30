package edu.iis.mto.staticmock;

import java.util.ArrayList;
import java.util.List;

public class PublishableNews {

    public static PublishableNews create() {
        return new PublishableNews();
    }

    private final List<String> publicContent = new ArrayList<>();
    private final List<String> subscribentContent = new ArrayList<>();

    public void addPublicInfo(String content) {
        this.publicContent.add(content);

    }

    public void addForSubscription(String content, SubsciptionType subscriptionType) {
        // TODO Auto-generated method stub
        // Komentarz AS do zadania 3_1 -nie jestem pewien, czy to jest do konca zgodne z trescia zadania, ale zeby
        // zapewnic
        // funkcjonalnosc ta implementacja i tak jest konieczna, wiec uznalem to za najlepsza opcje
        this.subscribentContent.add(content);
    }

}
