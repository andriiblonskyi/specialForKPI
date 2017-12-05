
function format(source, params) {
    $.each(params,function (i, n) {
        source = source.replace(new RegExp("\\{" + i + "\\}", "g"), n);
    })
    return source;
}

function formTable() {
    context["monitoring"] = false;
    var table = '<section>'+
        '        <h1>Список шлюзов</h1>'+
        '        <div class="tbl-header">'+
        '            <table cellpadding="0" cellspacing="0" border="0">'+
        '                <thead>'+
        '                <tr>'+
        '                    <th>Имя шлюза</th>'+
        '                    <th>Id</th>'+
        '                    <th>Состояние</th>'+
        '                    <th>Количество датчиков</th>'+
        '                </tr>'+
        '                </thead>'+
        '            </table>'+
        '        </div>'+
        '        <div class="tbl-content">'+
        '            <table cellpadding="0" cellspacing="0" border="0">'+
        '                <tbody>'+
        '               {0}'+
        '                </tbody>'+
        '            </table>'+
        '        </div>'+
        '    </section>';
        var template = '                <tr style="cursor: pointer;" onclick="selectDevice(\'{4}\',\'{5}\');">'+
            '                    <td>{0}</td>'+
            '                    <td>{1}</td>'+
            '                    <td>{2}</td>'+
            '                    <td>{3}</td>'+
            '                </tr>'+
            '                <tr>';
    var tableContent = "";
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/content/devicesList",
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            var json = JSON.parse(JSON.stringify(data));
            for (var i = 0; i < json.length; i++){
                var obj = json[i];
                var tmpContent = template;
                tableContent+= format(tmpContent, [obj["name"],obj["deviceId"],obj["status"],obj["sensorsCount"],obj["deviceId"],obj["name"]]);
            }
            $('#content-panel').empty();
            $('#content-panel').append(format(table,[tableContent]));
        },
        error: function (e) {

        }

    });
}
$( document ).ready(function() {
    context["monitoring"] = false;
    var template = '<li class="active" onclick="selectDevice(\'{0}\',\'{1}\');"><a class="device-name-link"> {1}</a></li>';

    $("#user-name-title").append(document.createTextNode(context.userName));

    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/content/loadDevicesList",
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            var json = JSON.parse(JSON.stringify(data));
            jQuery.each(json, function(key, val) {
                var tmp = template;
                var jsonValue = JSON.parse(JSON.stringify(val).replace("[","").replace("]",""));
                var item = format(tmp,[jsonValue["deviceId"],key])
                $("#devices").append(item);
            });
        },
        error: function (e) {

        }

    });
    updateNotificationsCount();


});

function updateNotificationsCount() {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/content/notificationsCount",
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            var json = JSON.parse(JSON.stringify(data));
            $(".badge").first().text(json["count"]);
        },
        error: function (e) {

        }

    });
}
setInterval(updateNotificationsCount,15000);

function selectDevice(id,name) {

    context["monitoring"] = false;

    var formatName = '<div id="device-name-value-panel">'+
        '            <h1 style="float: left;  width: 100%; color: black; font-size: 3vh;">Имя устройства:</h1>'+
        '            <input type="text" class="form-control" required="true" id="device_name" style="position: relative; left: 10%; float:left;width: 50%;">'+
        '            <button type="button" id="save-name-btn"class="btn btn-success" onclick="saveName(\'{0}\',\'{1}\');" style="float:left; position: relative; left: 10.4%; width: 25%;">Сохранить</button>'+
        '</div>';

    /*var formatName = '<h1 style=" position:absolute; float: left; left: 10%">Имя устройства:</h1>'+
        '<input type="text" class="form-control" required="true" id="device_name" style=" width: 60%;">'+
        '<button type="button" id="save-name-btn" class="btn btn-success" style=" width: 25%; " onclick="saveName(\'{0}\',\'{1}\');">Сохранить</button>';
    */
    var table = '<section>'+
        '        <h1>Список датчиков</h1>'+
        '        <div class="tbl-header">'+
        '            <table cellpadding="0" cellspacing="0" border="0">'+
        '                <thead>'+
        '                <tr>'+
        '                    <th>Тип датчика</th>'+
        '                    <th>Id датчика</th>'+
        '                </tr>'+
        '                </thead>'+
        '            </table>'+
        '        </div>'+
        '        <div class="tbl-content">'+
        '            <table cellpadding="0" cellspacing="0" border="0">'+
        '                <tbody>'+
        '               {0}'+
        '                </tbody>'+
        '            </table>'+
        '        </div>'+
        '    </section>';
    var template = '                <tr style="cursor: pointer;" onclick="selectSensor(\'{2}\');">'+
        '                    <td>{0}</td>'+
        '                    <td>{1}</td>'+
        '                </tr>'+
        '                <tr>';
    var tableContent = "";
//.replace(/\\/g,"").replace('"','').replace(/.$/,"")
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/content/sensorsList",
        dataType: 'json',
        data:id,
        cache: false,
        timeout: 600000,
        success: function (data) {
            var json = JSON.parse(JSON.stringify(data));
            for (var i = 0; i < json.length; i++){
                var obj = json[i];
                var tmpContent = template;
                tableContent+= format(tmpContent, [obj["type"],obj["sensorId"],obj["sensorId"]]);
            }
            $('#content-panel').empty();
            $('#content-panel').append(format(formatName,[id,name]));
            $('#content-panel').append(format(table,[tableContent]));
        },
        error: function (e) {
        }

    });
}
function saveName(id,name) {
    if ($("#device_name").val() == "")
    {
        alert("Значение должно быть не пустое!");
        return;
    }
    var search = {};
    search["name"] = $("#device_name").val();
    search["id"] = id;
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/content/changeName",
        dataType: 'json',
        data:JSON.stringify(search),
        cache: false,
        timeout: 600000,
        success: function (data) {
            if (data == true)
            {
                $('.device-name-link:contains(" '+name+'")').text(" "+$("#device_name").val());
                // here rewrite onclick, cause change only 1 time
                document.getElementById("save-name-btn").onclick = function(){ saveName(id,$("#device_name").val()); }
            }
            else
            {
                alert("Невозможно сменить имя");
            }
        },
        error: function (e) {
            alert("Невозможно сменить имя");
        }

    });
}
function findDevice() {
    var name = $("#search-input").val();
}




function selectSensor(id) {
    var critical_panel = '<div id="critical-value-panel">'+
        '            <h1 style="float: left; width: 100%; color: black; font-size: 3vh;">Минимальное критическое значение:</h1>'+
        '            <input type="text" class="form-control" required="true" id="min_critical_input" style="position: relative; left: 10%; float:left;width: 50%;">'+
        '            <button type="button" id="save-min-btn" class="btn btn-success" onclick="saveMinValue(\'{0}\');" style="float:left; position: relative; left: 10.4%; width: 25%;">Сохранить</button>'+
        ''+
        '            <h1 style="float: left;  width: 100%; color: black; font-size: 3vh;">Максимальное критическое значение:</h1>'+
        '            <input type="text" class="form-control" required="true" id="max_critical_input" style="position: relative; left: 10%; float:left;width: 50%;">'+
        '            <button type="button" id="save-max-btn" class="btn btn-success" onclick="saveMaxValue(\'{1}\');" style="float:left; position: relative; left: 10.4%; width: 25%;">Сохранить</button>'+
        '    </div>';

    var spline_panel = '<div style="width: 100%; height: 100%; position: relative; top:32%;">'+
        '<div class="demo-container">'+
        '        <div id="chart-demo">'+
        '            <div id="chart"></div>'+
        '            <div class="options">'+
        '                <div class="option">'+
        '                    <div id="types"></div>'+
        '                    <input type="text" id="date-picker" style="visibility:hidden">'+
        '                </div>'+
        '            </div>'+
        '        </div>'+
        '    </div>'+
        '</div>';

    $('#content-panel').empty();
    $('#content-panel').append(format(critical_panel, [id,id]));
    var getCriticalUrl = "/api/"+id+"/criticalValues";
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: getCriticalUrl,
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            var json = JSON.parse(JSON.stringify(data));
            $("#min_critical_input").val(json["min"]);
            $("#max_critical_input").val(json["max"]);
        },
        error: function (e) {
            alert("Ошибка на получение критических значений :" + e);
        }

    });


    var gauge = ' <div style="width: 100%; height: 45%; ">'+
        '<div class="demo-container">'+
        '        <div id="gauge"></div>'+
        '    </div>'+
        '</div>';

    $('#content-panel').append(gauge);
    context["monitoring"] = true;
    context["monitoringSensorId"] = id;


    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/content/sensorData",
        dataType: 'json',
        data:id,
        cache: false,
        timeout: 600000,
        success: function (data) {
            $("#gauge").dxCircularGauge({
                scale: {
                    startValue: 0,
                    endValue: 100,
                    tickInterval: 10,
                    label: {
                        useRangeColors: true
                    }
                },
                rangeContainer: {
                    ranges: [
                        { startValue: 0, endValue: 35, color: "#228B22" },
                        { startValue: 35, endValue: 70, color: "#FFD700" },
                        { startValue: 70, endValue: 100, color: "#CE2029" }
                    ]
                },
                title: {
                    text: "Текущее значение",
                    font: { size: 20 }
                },
                "export": {
                    enabled: true
                },
                value: data
            });

        },
        error: function (e) {
            alert("Похоже, устройство еще не прислало никаких данных. Подождите немного.");
            $("#gauge").dxCircularGauge({
                scale: {
                    startValue: 0,
                    endValue: 100,
                    tickInterval: 10,
                    label: {
                        useRangeColors: true
                    }
                },
                rangeContainer: {
                    ranges: [
                        { startValue: 0, endValue: 35, color: "#228B22" },
                        { startValue: 35, endValue: 70, color: "#FFD700" },
                        { startValue: 70, endValue: 100, color: "#CE2029" }
                    ]
                },
                title: {
                    text: "Текущее значение",
                    font: { size: 20 }
                },
                "export": {
                    enabled: true
                },
                value: 0
            });
        }

    });
    $('#content-panel').append(spline_panel);
    $("#date-picker").datepicker({dateFormat: 'dd-mm-yy'});
    $('#date-picker').on('change', function() {
        //select for day
        getForDayOf(id,$('#date-picker').val());
    });
    var types = ["День","Две недели","Выбрать день"];
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/"+id+"/day",
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            var json = JSON.parse(JSON.stringify(data));
            //alert(JSON.stringify(data));
            var splitRandomDate = [];
            for (var i = 0; i < json.length; i++){
                var obj = json[i];
                var param = new Object();
                param.date=parseDate(obj["date"]);
                param.value = obj["value"];
                splitRandomDate.push(param);
            }
            splitRandomDate.sort(function(a,b){
                return new Date(a.date) - new Date(b.date);
            });
            var splitDate = [];
            splitRandomDate.forEach(function (element, index, array) {
                var item = new Object();
                item.date=element.date.toTimeString();
                item.value = element.value;
                splitDate.push(item);
            })
            var chart = $("#chart").dxChart({
                theme:'generic.contrast',
                dataSource: splitDate,
                commonSeriesSettings: {
                    type: "spline",
                    argumentField: "date"
                },
                commonAxisSettings: {
                    grid: {
                        visible: true
                    }
                },
                argumentAxis: {
                    label: {
                        format: "HH:mm:ss"
                    }
                },
                margin: {
                    bottom: 20
                },
                series: [
                    { valueField: "value", name: "Значения" },
                ],
                tooltip:{
                    enabled: true
                },
                legend: {
                    verticalAlignment: "top",
                    horizontalAlignment: "right"
                },
                "export": {
                    enabled: true
                },
                title: "График значений за день."
            }).dxChart("instance");
            $("#types").dxSelectBox({
                dataSource: types,
                value: types[0],
                onItemClick: function(e){
                    if (e.itemData == "День")
                    {
                        getForDay(id);
                    }
                    else if (e.itemData == "Две недели")
                    {
                        getForWeek(id);
                    }
                    else if (e.itemData == "Выбрать день")
                    {
                        $('#date-picker').datepicker("show");
                    }
                }
            });

        },
        error: function (e) {
            alert("Ошибка на получение данных за день :" + e);
        }

    });


}
//here
setInterval(updateData,20000);

function getForDay(id) {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/"+id+"/day",
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            //alert(JSON.stringify(data));
            var json = JSON.parse(JSON.stringify(data));
            var splitRandomDate = [];
            for (var i = 0; i < json.length; i++){
                var obj = json[i];
                var param = new Object();
                param.date=parseDate(obj["date"]);
                param.value = obj["value"];
                splitRandomDate.push(param);
            }
            splitRandomDate.sort(function(a,b){
                return new Date(a.date) - new Date(b.date);
            });
            var splitDate = [];
            splitRandomDate.forEach(function (element, index, array) {
                var item = new Object();
                item.date=element.date.toTimeString();
                item.value = element.value;
                splitDate.push(item);
            })
            var chart = $("#chart").dxChart({
                theme:'generic.contrast',
                dataSource: splitDate,
                commonSeriesSettings: {
                    type: "spline",
                    argumentField: "date"
                },
                argumentAxis: {
                    label: {
                        format: "HH:mm:ss"
                    }
                },
                margin: {
                    bottom: 20
                },
                series: [
                    { valueField: "value", name: "Значения" },
                ],
                title: "График значений за день."
            }).dxChart("instance");
        },
        error: function (e) {
            alert("Ошибка на получение данных за день :" + e);
        }

    });
}
function getForDayOf(id,pattern) {
    var search = {};
    search["sensorId"] = id;
    search["pattern"] =pattern;
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/content/dayof",
        dataType: 'json',
        data:JSON.stringify(search),
        cache: false,
        timeout: 600000,
        success: function (data) {
            //alert(JSON.stringify(data));
            var json = JSON.parse(JSON.stringify(data));
            var splitRandomDate = [];
            for (var i = 0; i < json.length; i++){
                var obj = json[i];
                var param = new Object();
                param.date=parseDate(obj["date"]);
                param.value = obj["value"];
                splitRandomDate.push(param);
            }
            splitRandomDate.sort(function(a,b){
                return new Date(a.date) - new Date(b.date);
            });
            var splitDate = [];
            splitRandomDate.forEach(function (element, index, array) {
                var item = new Object();
                item.date=element.date.toTimeString();
                item.value = element.value;
                splitDate.push(item);
            })
            var chart = $("#chart").dxChart({
                theme:'generic.contrast',
                dataSource: splitDate,
                commonSeriesSettings: {
                    type: "spline",
                    argumentField: "date"
                },
                argumentAxis: {
                    label: {
                        format: "HH:mm:ss"
                    }
                },
                margin: {
                    bottom: 20
                },
                series: [
                    { valueField: "value", name: "Значения" },
                ],
                title: "График значений за " + pattern
            }).dxChart("instance");
        },
        error: function (e) {
            alert("Ошибка на получение данных за день." );
        }

    });
}

function getForWeek(id) {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/"+id+"/week",
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            //alert(JSON.stringify(data));
            var json = JSON.parse(JSON.stringify(data));
            var splitRandomDate = [];
            for (var i = 0; i < json.length; i++){
                var obj = json[i];
                var param = new Object();
                param.date=parseDate(obj["date"]);
                param.value = obj["value"];
                splitRandomDate.push(param);
            }
            splitRandomDate.sort(function(a,b){
                return new Date(a.date) - new Date(b.date);
            });
            var splitDate = [];
            splitRandomDate.forEach(function (element, index, array) {
                var item = new Object();
                item.date=element.date.toDateString();
                item.value = element.value;
                splitDate.push(item);
            })
            var chart = $("#chart").dxChart({
                theme:'generic.contrast',
                dataSource: splitDate,
                commonSeriesSettings: {
                    type: "spline",
                    argumentField: "date"
                },
                margin: {
                    bottom: 20
                },
                series: [
                    { valueField: "value", name: "Значения" },
                ],
                title: "График значений за две недели."
            }).dxChart("instance");
        },
        error: function (e) {
            alert("Ошибка на получение данных за неделю :" + e);
        }

    });
}

function parseDate(dateString) {
    var reggie = /(\d{2})-(\d{2})-(\d{4}) (\d{2}):(\d{2}):(\d{2})/;
    var dateArray = reggie.exec(dateString);
    var dateObject = new Date(
        (+dateArray[3]),
        (+dateArray[2])-1,
        (+dateArray[1]),
        (+dateArray[4]),
        (+dateArray[5]),
        (+dateArray[6])
    );
    return dateObject;

}
function isInt(value) {
    return !isNaN(value) &&
        parseInt(Number(value)) == value &&
        !isNaN(parseInt(value, 10));
}
function saveMinValue(id) {
    var search = {};
    search["sensorId"] = id;
    if (!isInt($("#min_critical_input").val()))
    {
        alert("Параметр должен быть числом.");
        return;
    }
    search["value"] = parseInt($("#min_critical_input").val());
    //alert(JSON.stringify(search));
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/content/updateMin",
        dataType: 'json',
        data:JSON.stringify(search),
        cache: false,
        timeout: 600000,
        success: function (data) {
            if (data == true)
                alert("Значение обновлено.")
            else
                alert("Ошибка обновления.");
        },
        error: function (e) {
            alert("Ошибка обновления.");
        }

    });
}

function saveMaxValue(id) {
    var search = {}
    search["sensorId"] = id;
    if (!isInt($("#max_critical_input").val()))
    {
        alert("Параметр должен быть числом.");
        return;
    }
    search["value"] = parseInt($("#max_critical_input").val());
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/content/updateMax",
        dataType: 'json',
        data:JSON.stringify(search),
        cache: false,
        timeout: 600000,
        success: function (data) {
            alert("Значение обновлено.")
        },
        error: function (e) {
            alert("Ошибка обновления :" + e);
        }

    });
}

function updateData() {
    if (context["monitoring"])
    {

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/content/sensorData",
            dataType: 'json',
            data:context["monitoringSensorId"],
            cache: false,
            timeout: 600000,
            success: function (data) {
                $("#gauge").dxCircularGauge({
                    value: data
                });
            },
            error: function (e) {
            }

        });
    }
}

function formAnalytic() {
    /*context["monitoring"] = false;
    var analyticTable = '<div class="card"></div>'+
        ' <div class="card"></div>'+
        ' <div class="card"></div>';

    $('#content-panel').empty();
    $('#content-panel').append(analyticTable);*/

}

function formNotifications() {
    context["monitoring"] = false;
    var notificationTemplate = '<div class="notification-card">'+
        '        <img class="error-type-img" src="../../css/error-notification.png">'+
        '        <h1 style="color: black; font-size: 17px;">{0}</h1>'+
        '        <h1 style="color: black; font-size: 15px;">{1}</h1>'+
        '    </div>';
    var result = "";
    $('#content-panel').empty();
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/content/notifications",
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            var json = JSON.parse(JSON.stringify(data));
            if (json.length != 0)
            {
                for (var i = 0; i < json.length; i++){
                    var obj = json[i];
                    var tmpContent = notificationTemplate;
                    result += format(tmpContent, [obj["title"],obj["msg"]]);
                }
            }
            $('#content-panel').append(result);
            $(".badge").first().text("0");
        },
        error: function (e) {

        }

    });
}

$(window).on("load resize ", function() {
    var scrollWidth = $('.tbl-content').width() - $('.tbl-content table').width();
    $('.tbl-header').css({'padding-right':scrollWidth});
}).resize();