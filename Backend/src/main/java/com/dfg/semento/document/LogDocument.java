package com.dfg.semento.document;

import com.dfg.semento.enums.Mode;
import com.dfg.semento.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Document(indexName = "#{'semento-mysql-logs-'+T(java.time.LocalDate).now()}")
public class LogDocument {

    @Id
    @Field(type= FieldType.Keyword, name = "doc_id")
    public String docId;

    @Field(type= FieldType.Long, name = "oht_id")
    public Long ohtId;

    @Field(type= FieldType.Keyword, name = "mode")
    public Mode mode;

    @Field(type= FieldType.Keyword, name = "status")
    public Status status;

    @Field(type= FieldType.Keyword, name = "current_node")
    public String currentNode;

    @Field(type= FieldType.Keyword, name = "next_node")
    public String nextNode;

    @Field(type= FieldType.Keyword, name = "target_node")
    public String targetNode;

    @Field(type= FieldType.Boolean, name = "carrier")
    public boolean carrier;

    @Field(type= FieldType.Keyword, name = "error")
    public int error;

    @Field(type= FieldType.Boolean, name = "oht_connect")
    public boolean ohtConnect;

    @Field(type= FieldType.Double, name = "curr_node_offset")
    public double currNodeOffset;

    @Field(type= FieldType.Double, name = "speed")
    public double speed;

    @Field(type= FieldType.Boolean, name = "is_fail")
    public boolean isFail;

    @Field(type= FieldType.Date, name = "curr_time")
    public LocalDateTime currTime;

    @Field(type= FieldType.Date, name = "start_time")
    public LocalDateTime startTime;

    @Field(type= FieldType.Double, name = "point_x")
    public double pointX;

    @Field(type= FieldType.Double, name = "point_y")
    public double pointY;

    @Field(type= FieldType.Keyword, name = "path")
    public String path;
}
