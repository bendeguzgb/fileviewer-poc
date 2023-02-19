<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Error</title>
</head>
<body>
    <#if message?has_content>
        <#assign message = message>
    <#else>
        <#assign message = "">
    </#if>

    <#if type?has_content>
        <#assign type = type>
    <#else>
        <#assign type = "">
    </#if>

    <h3>Error: '${type}'</h3>
    <h3>Message: '${message}'</h3>
</body>
</html>