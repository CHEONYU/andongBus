package co.kr.smartplusteam.luna.study.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Document(indexName = "rawdata", type = "rawdata", createIndex = false)

public class RawData {
    // ID
    @Id
    private String id;
    
    //등록시간
    @Field(type = FieldType.Date)
    private String regiTime;

    //호출 URL
	@Field(type = FieldType.Text, index = false)
    private String apiUrl;

	//파라미터
    @Field(type = FieldType.Text, index = false)
    private String parameters;

	//원문
    @Field(type = FieldType.Text, index = false)
    private String rawData;
}
