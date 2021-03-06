<%--
  Created by IntelliJ IDEA.
  User: cmw天下第一
  Date: 2021/5/9
  Time: 13:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>File Tree</title>
    <script src="jQuery.js"></script>
    <script type="text/javascript" src="jstree-master/dist/jstree.min.js"></script>
    <!--        <script type="text/javascript" src="jstree-master/src/jstree."></script>-->
    <link rel="stylesheet" href="jstree-master/dist/themes/default/style.min.css"/>
    <script type="text/javascript">
        var jsonArray = []

        function drawJsTree(data) {
            console.log(data)
            $("#jstree").jstree({
                "core": {
                    "data": data
                },
                "plugins": ["types"],
                "types": {
                    "root": {
                        "icon": "picture/doc.png",
                    },
                    "file": {
                        "icon": "picture/java.png",
                    },
                    "function": {
                        "icon": "picture/function.png",
                    }
                }
            })
        }

    </script>
</head>
<body>
<input id="path" type="text"/>
<button id="submit">提交</button>
<script type="text/javascript">
    $("#submit").click(function () {
        console.log($("#path").val())
        $.post({
            url: "test",
            data: {
                status: "1",
                path: $("#path").val()
            },
            success: function (data) {
                console.log(data)
                if (data === "ok") {
                    $.getJSON("test.json", function (result) {
                        // console.log(result)
                        jsonArray = result
                        // console.log(jsonArray)
                        jsonArray[0].parent = "#"
                        drawJsTree(jsonArray)
                    })
                }
            }
        })
    })
</script>
<div id="jstree"></div>

<script>
    // drawJsTree()
    var jstree = document.getElementById("jstree")
    jstree.onclick = function (ev) {
        ev = ev || window.event;
        let target = ev.target || ev.srcElement;
        if (target.nodeName.toLocaleLowerCase() == 'a') {
            console.log(target.id)
            let id = target.id.split("_")[0]

            for (let i = 0; i < jsonArray.length; i++) {
                if (jsonArray[i].id = id) {
                    if (jsonArray[i].type = "function") {
                        console.log(id)
                        $.post({
                            url: "test",
                            data: {
                                status: "2",
                                code: id
                            },
                            success: function (data) {
                                console.log(data)
                            }
                        })
                    }
                }
            }
        }
    }
</script>
</body>
</html>


