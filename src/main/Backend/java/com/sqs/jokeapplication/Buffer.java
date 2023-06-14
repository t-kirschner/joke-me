package com.sqs.jokeapplication;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
public class Buffer {

    @Autowired
    private JokeRepository repository;
    Api api = new Api();

    @PostConstruct
    public void initJokes() {
        for (int i = 0; i < 10; i++) {
            Joke jokeGerman = new Joke();
            Joke jokeEnglish = new Joke();
            jokeGerman.setLanguage(Set.GERMAN.toString());
            jokeGerman.setJoke(api.getApiResponse(Set.GERMAN));
            jokeEnglish.setLanguage(Set.ENGLISH.toString());
            jokeEnglish.setJoke(api.getApiResponse(Set.ENGLISH));
            repository.save(jokeGerman);
            repository.save(jokeEnglish);
        }
    }

    public void loadJokes(Set language) {
        for (int i = 0; i < 10; i++) {
            Joke joke = new Joke();
            joke.setLanguage(language.toString());
            joke.setJoke(api.getApiResponse(language));
            repository.save(joke);
        }
    }


    @GetMapping("/")
    public String getJoke(String userInput) {
        String language;
        if (userInput.contains("english")) {
            language = "ENGLISH";
        } else {
            language = "GERMAN";
        }

        Joke joke = repository.findFirstByLanguage(language);
        repository.delete(joke);

        return joke.getJoke();
    }



    public long getCountByLanguage(String language) {
        return repository.countByLanguage(language);
    }
}

