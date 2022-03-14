package za.co.example.reactivetweetapi.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Document(collection = "tweets")
public class Tweet {

    @Id
    private String id;
    @NotBlank
    @Size(max = 140)
    private String text;
    @CreatedDate
    private String created;
    @LastModifiedDate
    private String lastModified;

}
