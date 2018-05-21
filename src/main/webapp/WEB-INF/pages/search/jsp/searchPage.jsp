<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="java.io.File"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<!-- saved from url=(0028)https://searchcode.com/?q=ad -->
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta name="description"
          content="searchcode is a free source code and documentation search engine. API documentation, code snippets and open source (free sofware) repositories are indexed and searchable."/>
    <link rel="shortcut icon" href="https://searchcode.com/static/favicon.ico" type="image/x-icon"/>
    <title>Search for ad | source code search engine</title>
    <link href="../search/static/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="../search/static/css/style.css" rel="stylesheet"/>
    <link href="../search/static/css/jquery.nouislider.css" rel="stylesheet"/>
    <link rel="search" type="application/opensearchdescription+xml" title="searchcode"
          href="https://addons.mozilla.org/firefox/downloads/latest/570226/addon-570226-latest.xml"/>
    <script async="" src="https://www.google-analytics.com/analytics.js"></script>
    <script type="text/javascript" async="" src="https://portfold.com/code/1/"></script>
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
            <li><a href="https://searchcode.com/about/"><strong>admin management</strong></a></li>
        </ul>
    </div>
</nav>
<div class="container">
    <div>
        <div class="search-options">
            <form action="../search/searchCode.html" method="get">
                <div class="form-inline">
                    <div class="form-group">
                        <input autocapitalize="off" autocorrect="off" autocomplete="off" spellcheck="true" size="50"
                               placeholder="Search Expression" type="search" class="form-control" name="clause"/>
                        <input hidden="true" name="pageIndex" value="1"/>
                    </div>
                    <input type="submit" value="search" class="btn btn-success"/>
                </div>
            </form>
        </div>
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
        <div class="row search-count" style="border-bottom: 1px solid #eee;
    padding: 10px;
    margin-top: 10px;">
            <b>About ${totalSize} results: </b>
            <span class="grey">&quot;${searchClause}&quot;</span>
        </div>
        <div class="row">
            <div class="col-md-3 search-filters-container search-filters">
                <div>
                    <h5>Page ${currentPage} of ${pageCount}</h5>
                    <div class="center">
                        <a disabled="disabled" class="btn btn-xs btn-success filter-button"
                           href="https://searchcode.com/?q=ad&amp;loc=0&amp;loc2=10000&amp;p=-1">◀ Previous</a>
                        <a class="btn btn-xs btn-success filter-button"
                           href="https://searchcode.com/?q=ad&amp;loc=0&amp;loc2=10000&amp;p=1">Next ▶</a>
                    </div>
                </div>
                <center>
                    <div class="search-filters-ad">
                        <script async="" type="text/javascript" src="../search/static/js/carbon.js"
                                id="_carbonads_js"></script>
                    </div>
                </center>
                <form action="./searchCodeFilter.html" method="post">
                    <div>
                        <h5>Repository</h5>
                        <c:forEach var="repository" items="${repositoryCount.entrySet()}">
                            <div class="checkbox">
                                <label> <input type="checkbox" name="repository" value="${repository.getElement()}"/>
                                    <span>${repository.getElement()}</span><span
                                            class="badge pull-right">${repository.getCount()}</span> </label>
                            </div>
                        </c:forEach>
                    </div>
                    <div>
                        <h5>Languages</h5>

                        <c:forEach var="language" items="${languageCount.entrySet()}">
                            <div class="checkbox">
                                <label> <input type="checkbox" name="language" value="${language.getElement()}"/>
                                    <span>${language.getElement()}</span><span
                                            class="badge pull-right">${language.getCount()}</span> </label>
                            </div>
                        </c:forEach>
                    </div>
                    <input hidden name="clause" value="${searchClause}"/>
                    <input hidden name="pageIndex" value="${currentPage}"/>
                    <div>
                        <h5>Filter Results</h5>
                        <div class="center">
                            <a href="../search/searchCode.html?clause=${searchClause}"
                               class="btn btn-xs btn-success filter-button">Remove</a>
                            <input type="submit" value="Apply" class="btn btn-xs btn-success filter-button">
                        </div>
                    </div>
                </form>
                <div>
                    <h5 id="try-search-on">Try Search On</h5>
                    <ul class="nav nav-pills nav-stacked sidebar">
                        <li><a href="https://github.com"> GitHub Code </a></li>
                        <li><a href="https://gitlab.com/explore"> Gitlab Code </a></li>
                    </ul>
                </div>
            </div>

            <div class="col-md-9 search-results" id="codeFileList">
                <c:choose>
                    <c:when test="${hitList.isEmpty()}">
                        <h4>No results found :(</h4>
                        <p>Try Search On:
                            <a href="https://github.com/search?p=&amp;q=${searchClause}&amp;type=Code">Github</a> |
                            <a href="http://stackoverflow.com/search?q=${searchClause}">StackOverflow</a>
                        </p>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${hitList}" var="hitDocument" begin="0" end="10">
                            <div class="code-result">
                                <div>
                                    <h5>
                                        <a href="../searchResource/searchFile.html?filePath=<c:out value="${hitDocument.path.replaceAll(\"</?strong>\",\"\")}"/>">${hitDocument.path.substring(hitDocument.path.indexOf(File.separator,hitDocument.path.indexOf(File.separator) + 1) + 1,hitDocument.path.length())}</a>
                                        <small><a href="${hitDocument.gitPath}">git地址:${hitDocument.gitPath}</a>
                                            | ${hitDocument.language}
                                        </small>
                                    </h5>
                                </div>
                                <ol class="code-result">
                                    <c:set var="contents" value="${hitDocument.content.split(\"\\\\n\")}"/>
                                    <c:forEach items="${contents}" var="content" begin="0" end="15">
                                        <li><a href="../searchResource/searchFile.html?filePath=${hitDocument.path}">
                                                <%--<pre><c:out value="${content}"/></pre>--%>
                                            <pre>${content}</pre>
                                        </a></li>
                                    </c:forEach>
                                </ol>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>

                <hr class="spacer"/>
            </div>

            <div class="search-pagination">
                <ul id="pages" class="pagination">
                    <%
                        int currentPage = (int) request.getAttribute("currentPage");
                        int totalPage = (int) request.getAttribute("pageCount");
                        int start = 0;
                        int end = 0;
                        if (currentPage - 5 <= 0 && currentPage + 5 > totalPage) {
                            start = 1;
                            end = totalPage;
                        } else if (currentPage - 5 <= 0) {
                            start = 1;
                            end = 9;
                        } else if (currentPage + 5 > totalPage) {
                            start = totalPage - 9;
                            end = totalPage;
                        } else {
                            start = currentPage - 4;
                            end = currentPage + 4;
                        }
                        for (int k = start; k <= end; k++) {
                            if (k == currentPage) {
                    %>
                    <li class="active"><a href="#codeFileList"><%=k%>
                    </a></li>
                    <%
                    } else {
                    %>
                    <li><a href="../search/searchCodeFilter.html?clause=${searchClause}&pageIndex=<%=k%>&repository=${selectRepository}&language=${selectLanguage}"><%=k%>
                    </a></li>
                    <%
                            }
                        }
                    %>
                </ul>
            </div>
            <a href="https://searchcode.com/?q=ad#" class="back-to-top" style="display: none;">Back to Top</a>
        </div>
    </div>
    <div class="footer">
        <p></p>
        <ul>
            <li><a href="https://searchcode.com/about/">admin management</a></li>
        </ul>
        <p></p>
        <p></p>
        <!--End mc_embed_signup-->
        <p></p>
        <p>searchcode is proudly Made in kxxfydj | &copy; kxxfydj 202018</p>
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
        dc.src = '//portfold.com/code/1/';
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(dc, s);
    })();
</script>
</body>
</html>