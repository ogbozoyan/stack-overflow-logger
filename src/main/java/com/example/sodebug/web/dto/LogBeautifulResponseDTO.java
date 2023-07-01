package com.example.sodebug.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author ogbozoyan
 * @date 20.06.2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogBeautifulResponseDTO implements Serializable {
    private Long id;
    private Object num;
    private String fio;
    private String action;
    private String entity;
    @JsonIgnore
    private String processName;
    private Timestamp date;

    public void setAction(String action) {
        switch (action) {
            case "CREATE" -> this.action = "Создание";
            case "READ" -> this.action = "Чтение";
            case "UPDATE" -> this.action = "Обновление";
            case "DELETE" -> this.action = "Удаление";
            case "SIGN_IN" -> this.action = "Вход";
            case "SIGN_UP" -> this.action = "Регистрация";
            case "UNDEFINED" -> this.action = "Неопределенное действие";
            case "SIGN_OUT" -> this.action = "Выход";
            case "CAMUNDA" -> this.action = "Старт процесса";
        }
    }

    public String getProcessName() {
        if (processName == null)
            return "";
        return processName;
    }

    public void setEntity(String entity) {
        this.entity = entity + ' ' + getProcessName();
    }

}
