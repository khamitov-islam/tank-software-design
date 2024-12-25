package ru.mipt.bit.platformer.abstractions.config.conditions;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import ru.mipt.bit.platformer.abstractions.config.Config;

public class DefaultConfigCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return Config.DEFAULT.equals(context.getEnvironment().getProperty("game.config", Config.class));
    }
}