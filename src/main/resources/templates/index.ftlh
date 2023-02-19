<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home</title>
</head>
<body>
    <h2>FileViewer</h2>
    <p> <b>Path: ${model.path}</b></p>
        <ul>
            <#list model.files as file>
                <#if RequestParameters.path?has_content>
                    <#assign relativePath = RequestParameters.path + "/" + file.fileName>
                <#else>
                    <#assign relativePath = "/" + file.fileName>
                </#if>
                <li>
                    <#if file.directory>
                        <a href="home?path=${relativePath}">${file.fileName}</a>
                    <#else>
                        <a href="download?path=${relativePath}">${file.fileName}</a>
                    </#if>
                </li>
            </#list>
        </ul>
</body>
</html>