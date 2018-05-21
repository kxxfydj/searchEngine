<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="java.io.File"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<!-- saved from url=(0070)https://searchcode.com/file/1962134/Avidemux/autononreg/fpstest.js#l-1 -->
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta name="description" content="fpstest.js in mulder located at /Avidemux/autononreg"/>
    <link rel="shortcut icon" href="https://searchcode.com/static/favicon.ico" type="image/x-icon"/>
    <title>fpstest.js in mulder | source code search engine</title>
    <link href="../search/static/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="../search/static/css/style.css" rel="stylesheet"/>
    <link href="../search/static/css/jquery.nouislider.css" rel="stylesheet"/>
    <link rel="search" type="application/opensearchdescription+xml" title="searchcode"
          href="https://addons.mozilla.org/firefox/downloads/latest/570226/addon-570226-latest.xml"/>
    <script async="" src="https://www.google-analytics.com/analytics.js"></script>
    <script src="../search/static/js/jquery-1.11.1.min.js"></script>
    <script id="_bsa_srv-CVAIVKJU_0" type="text/javascript" src="../search/static/json/CVAIVKJU.json"></script>
    <script id="_carbonads_projs" type="text/javascript" src="../search/static/json/C6AILKT.json"></script>
</head>
<body>
<nav class="navbar navbar-default" role="navigation">
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
            <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span>
            <span class="icon-bar"></span></button>
        <a class="navbar-brand" href="https://searchcode.com/"> <img height="24px"
                                                                     src="../search/static/img/searchcode_logo.png"/>
        </a>
    </div>
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
        <ul class="nav navbar-nav navbar-right">
            <li><a href="https://searchcode.com/about/">admin management</a></li>
        </ul>
    </div>
</nav>
<div class="container">
    <div class="search-options">
        <form action="https://searchcode.com/">
            <div class="form-inline">
                <div class="form-group">
                    <input autocapitalize="off" autocorrect="off" autocomplete="off" spellcheck="true" size="50"
                           placeholder="Search Expression" type="search" class="form-control" name="q" value=""/>
                </div>
                <input type="submit" value="search" class="btn btn-success"/>
            </div>
        </form>
    </div>
    <div class="row">
        <script src="../search/static/js/monetization.js" type="text/javascript"></script>
        <div id="ad_top"></div>
        <script>
            (function () {
                // format, zoneKey, segment:value, options
                _bsa.init('default', 'CVAIVKJU', 'placement:demo', {
                    target: '#ad_top',
                    align: 'horizontal',
                    disable_css: 'true'
                });
            })();
        </script>
        <h4 class="codepath"><a href="http://www.${codeInfo.repository}.com/${codeInfo.author}">${codeInfo.author}</a>
            ${codeContent.path.substring(codeContent.path.indexOf(File.separator,codeContent.path.indexOf(File.separator) + 1) + 1,codeContent.path.length())} </h4>
        <table class="table">
            <tbody>
            <tr>
                <td colspan="5">
                    <div>
                        <script async="" type="text/javascript" src="../search/static/js/carbon.js"
                                id="_carbonads_js"></script>
                        <style>#carbonads {
                            margin: 0 auto;
                            padding: 1em;
                            max-width: 26em !important;
                            border: solid 2px #428bca;
                            border-radius: 3px;
                            font-size: .9em;
                            font-family: Verdana, sans-serif;
                            line-height: 1.5;
                        }

                        #carbonads * {
                            display: block;
                            overflow: hidden;
                        }

                        .carbon-img {
                            float: left;
                            margin-right: 1em !important;
                            margin-bottom: .5em;
                        }

                        .carbon-text {
                            float: left;
                            max-width: 12em;
                            text-align: left;
                        }

                        .carbon-poweredby {
                            margin-top: -1em;
                            text-align: right;
                            font-size: .9em !important;
                        }</style>
                    </div>
                </td>
            </tr>
            <tr>
                <td>Language</td>
                <td>${codeInfo.language}</td>
                <td> Lines</td>
                <c:set var="contents" value="${codeContent.body.split(\"\\\\n\")}"/>
                <td>${fn:length(contents)}</td>
            </tr>
            <tr>
                <td> Repository</td>
                <td>${codeInfo.gitPath}</td>
            </tr>
            <tr>
                <td colspan="5">
                    <a href="#" rel="nofollow" id="file-tree-link" class="">
                        <span class="glyphicon glyphicon-tree-conifer"></span> <span id="file-tree-button" data-id="${codeInfo.id}">View File Tree</span>
                    </a>
                </td>
            </tr>
            </tbody>
        </table>
        <div class="row">
            <div class="col-md-4" id="file-tree">
                <div id="file-tree-list">
                </div>
            </div>
            <div class="col-md-8">
                <table class="highlighttable">
                    <tbody>
                    <tr>
                        <td class="linenos">
                            <div class="linenodiv">
<pre>
<c:forEach var="i" begin="1" end="${fn:length(contents)}"
step="1">
<c:out value="${i}"/>
</c:forEach>
</pre>
                            </div>
                        </td>
                        <td class="code">
                            <div class="highlight">
<pre>
<c:forEach var="content" items="${contents}">
<c:out value="${content}"/>
</c:forEach>
</pre>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <a href="#file-tree-list" class="back-to-top"
           style="display: none;">Back to Top</a>
        <div class="footer">
            <p></p>
            <ul>
                <li><a href="https://searchcode.com/about/">admin management</a></li>
            </ul>
            <p></p>
            <p></p>
            <!--End mc_embed_signup-->
            <p></p>
            <p>searchcode is proudly Made in kxxfydj | &copy; kxxfydj 2018</p>
            <script async="" src="../search/static/js/script.js"></script>
            <script async="" src="../search/static/js/jquery.nouislider.min.js"></script>
            <script async="" src="../search/static/js/bootstrap.min.js"></script>
            <script async="" src="../search/static/js/typeahead.bundle.min.js"></script>
        </div>
    </div>
    <script>
        (function () {
            var dc = document.createElement('script');
            dc.type = 'text/javascript';
            dc.async = true;
            dc.src = 'directory_tree/${codeInfo.id}/';
            var s = document.getElementsByTagName('script')[0];
            s.parentNode.insertBefore(dc, s);
        })();
    </script>
</div>
</body>
</html>