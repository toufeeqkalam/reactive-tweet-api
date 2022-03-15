package za.co.example.reactivetweetapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import za.co.example.reactivetweetapi.model.Tweet;
import za.co.example.reactivetweetapi.repository.TweetRepository;

import javax.validation.Valid;

@RestController
public class TweetController {

    private static final Logger logger = LoggerFactory.getLogger(TweetController.class);

    @Autowired
    TweetRepository tweetRepository;

    @GetMapping(value = "/tweet/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<?>> getTweetById(@PathVariable String id) {
        return this.tweetRepository.findById(id).flatMap(tweet -> Mono.just(new ResponseEntity<>(tweet, HttpStatus.OK)));
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<?>> getAllTweets() {
        return this.tweetRepository.findAll(Sort.by(Sort.Direction.DESC, "created")).collectList().flatMap(tweets -> {
            if (tweets.isEmpty()) {
                return Mono.just(new ResponseEntity<>(tweets, HttpStatus.OK));
            } else {
                return Mono.just(new ResponseEntity<>(HttpStatus.NO_CONTENT));
            }
        });
    }

    @PostMapping(value = "/tweet", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Tweet>> saveTweet(@RequestBody @Valid Tweet tweet) {
        return this.tweetRepository.save(tweet)
                .map(response -> new ResponseEntity<>(tweet, HttpStatus.OK));
    }

    @PutMapping("/tweet/{id}")
    public Mono<ResponseEntity<Tweet>> updateTweet(@PathVariable String id, @Valid @RequestBody Tweet tweet) {
        return tweetRepository.findById(id).flatMap(existingTweet -> {
                    existingTweet.setText(tweet.getText());
                    return tweetRepository.save(existingTweet);
                })
                .map(updateTweet -> new ResponseEntity<>(updateTweet, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(value = "/tweet/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> deleteTweetById(@PathVariable String id) {
        return tweetRepository.findById(id).flatMap(existingTweet -> this.tweetRepository.delete(existingTweet)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }
}
