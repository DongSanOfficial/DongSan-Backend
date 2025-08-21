package com.dongsan.domain.config;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import jakarta.annotation.PostConstruct;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Configuration
public class P6spyConfig implements MessageFormattingStrategy {
    @PostConstruct
    public void setLogMessageFormat() {
        P6SpyOptions.getActiveInstance()
                .setLogMessageFormat(this.getClass()
                        .getName());
    }

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared,
                                String sql, String url) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        String currentTime = formatter.format(new Date());
        String threadName = Thread.currentThread().getName();
        String formattedSql = formatSqlOneLine(category, sql);

        return String.format("%s | Operation Time : %dms | Thread: %s\n%s",
                currentTime, elapsed, threadName, formattedSql);
    }

    private String formatSql(String category, String sql) {
        if (sql == null || sql.trim()
                .equals("")) {
            return sql;
        }

        if (Category.STATEMENT.getName()
                .equals(category)) {
            String tmpsql = sql.trim()
                    .toLowerCase(Locale.ROOT);
            if (tmpsql.startsWith("create") || tmpsql.startsWith("alter") || tmpsql.startsWith("drop")
                    || tmpsql.startsWith("comment")) {
                sql = FormatStyle.DDL.getFormatter()
                        .format(sql);
            } else {
                sql = FormatStyle.BASIC.getFormatter()
                        .format(sql);
            }
        }
        return sql;
    }

    private String formatSqlOneLine(String category, String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            return sql;
        }

        // DDL, DML 등 모두 한 줄로
        return sql.replaceAll("\\s+", " ").trim();
    }
}
