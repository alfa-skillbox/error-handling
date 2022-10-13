package ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.helpers;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.entities.Error;
import ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.entities.ErrorId;
import ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.entities.ErrorMeta;
import ru.alfabank.skillbox.examples.errorhandling.exceptions.practice.repositories.ErrorsRepository;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * A guy who gets the data from the repo and transforms into Error domains
 */
@Getter
@Setter
public class ErrorBuilder {

    @Value("${errors.default.title:Ошибка обработки данных}")
    private String defaultTitle;

    @Value("${errors.default.statusCode:500}")
    private int defaultStatusCode;

    @Value("${errors.metaEnabled:true}")
    private Boolean metaEnabled;

    @Autowired(required = false)
    private List<ErrorsRepository> repositories = new ArrayList<>();

    /**
     * Produces common error response builder
     * <p>
     * Fill `detail` field for human text and `meta` for tech message.
     * <p>
     * If field `title` is omitted it will try to fetch title from some repository
     * or use the {@link #defaultTitle}
     */
    public ErrorPrototype forErrorId(ErrorId id) {
        return new ErrorPrototype(id);
    }

    @Data
    @Accessors(chain = true, fluent = true)
    public class ErrorPrototype {
        private final ErrorId id;

        private Integer status;
        private String title;
        private String detail;
        private ErrorMeta meta;

        public Error build() {
            return new Error().setDetail(detail)
                .setMeta(FALSE.equals(metaEnabled) ? null : meta)
                .setCode(id.getOuterCode())
                .setTitle(!isBlank(title) ? title : determineTitle(id.getInnerCode(), defaultTitle))
                .setStatus(!isNull(status) ? status : defaultStatusCode);
        }

        public ErrorPrototype status(HttpStatus status) {
            this.status = status.value();
            return this;
        }

        /**
         * Fetches error title from some repository (possibly external service)
         * by {@param innerCode}.
         * <p>
         * If absent - use {@link #defaultTitle}
         */
        private String determineTitle(String innerCode, String defaultTitle) {
            if (isBlank(id.getInnerCode())) {
                return defaultTitle;
            }
            return repositories.stream()
                .map(repository -> repository.getErrorMessage(innerCode))
                .filter(title -> !isBlank(title))
                .findFirst()
                .orElse(defaultTitle);
        }
    }
}
