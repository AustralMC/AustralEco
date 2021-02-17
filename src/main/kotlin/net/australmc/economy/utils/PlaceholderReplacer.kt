package net.australmc.economy.utils

import org.apache.commons.text.StringSubstitutor

private const val PLACEHOLDER_PREFIX = "{"
private const val PLACEHOLDER_SUFFIX = "}"

fun replacePlaceholders(template: String, placeholders: Map<String, String>): String =
        StringSubstitutor.replace(template, placeholders, PLACEHOLDER_PREFIX, PLACEHOLDER_SUFFIX)