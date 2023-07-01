package com.example.sodebug.aspect.logger.model;

import com.example.sodebug.aspect.logger.model.json.ActionDomainEnum;
import com.example.sodebug.aspect.logger.model.json.ActionEnum;
import com.example.sodebug.aspect.logger.model.json.HttpMethodEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * The {@code LogEntity} class represents a log entity in the system.
 *
 * <p>It is annotated with {@code @Entity} and {@code @Table} annotations to specify the database table mapping.</p>
 *
 * <p>Properties:</p>
 * <ul>
 *     <li>{@link #id}: The unique identifier of the log entity</li>
 *     <li>{@link #userId}: The user ID associated with the log</li>
 *     <li>{@link #userLogin}: The login name of the user</li>
 *     <li>{@link #httpMethodEnum}: The HTTP method used in the request</li>
 *     <li>{@link #url}: The URL of the request</li>
 *     <li>{@link #action}: The action performed</li>
 *     <li>{@link #actionDomain}: The domain of the action</li>
 *     <li>{@link #requestDataChange}: The request data change information</li>
 *     <li>{@link #responseDataAfterChange}: The response data after a change operation</li>
 *     <li>{@link #actionStatus}: The status of the action</li>
 *     <li>{@link #responseStatus}: The status of the response</li>
 *     <li>{@link #dtCreate}: The timestamp of when the log was created</li>
 *     <li>{@link #baseException}: The base exception associated with the log</li>
 *     <li>{@link #stackTraceOnError}: The stack trace on error</li>
 * </ul>
 *
 * @author ogbozoyan
 * @date 17.03.2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "system_f_log", schema = "public")
public class LogEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "user_login")
    private String userLogin;
    @Column(name = "http_method")
    @Enumerated(EnumType.STRING)
    private HttpMethodEnum httpMethodEnum;
    @Column(name = "url")
    private String url;
    @Column(name = "action")
    @Enumerated(EnumType.STRING)
    private ActionEnum action;
    @Column(name = "action_domain")
    @Enumerated(EnumType.STRING)
    private ActionDomainEnum actionDomain;
    @Column(name = "request_data_change", columnDefinition = "varchar")
    private String requestDataChange;
    @Column(name = "response_data_after_change", columnDefinition = "varchar")
    private String responseDataAfterChange;
    @Column(name = "action_status")
    private String actionStatus;
    @Column(name = "response_status")
    private String responseStatus;
    @Column(name = "dt_create")
    private Timestamp dtCreate;
    @Column(name = "base_exception")
    private String baseException;
    @Column(name = "stack_trace_on_error")
    private String stackTraceOnError;
}
