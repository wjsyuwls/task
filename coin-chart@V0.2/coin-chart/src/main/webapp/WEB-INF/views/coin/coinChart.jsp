<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Chart</title>
    <link href="/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="/resources/css/style.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
        google.charts.load('current', {'packages': ['line', 'controls']});
        google.charts.setOnLoadCallback(drawDashboard);

        function drawDashboard() {

            const ticker = $('.select_ticker .symbol').get();
            let t_arr = [];
            for (let i = 0; i < ticker.length; i++) {
                t_arr.push(ticker[i].innerHTML);
            }

            $.ajax({
                url: '/coin',
                type: 'post',
                data: {
                    'tickers': t_arr,
                },
                dataType: "json",
                success: function (coin) {

                    var data = new google.visualization.DataTable();

                    /** 그래프에 표시할 컬럼 추가 */
                    data.addColumn('datetime', 'Day');

                    let symbols = new Array();
                    for (key in coin) {
                        symbols.push(key);
                        data.addColumn('number', key);
                    }

                    var years = [];
                    var months = [];
                    var days = [];
                    var hours = [];
                    var minutes = [];

                    /** 날짜 기준은 첫번째 티커 */
                    for (let i = 0; i < coin[symbols[0]].length; i++) {
                        let date = coin[symbols[0]][i].saveDateTime.split('-');
                        let time = coin[symbols[0]][i].saveDateTime.split(' ')[1].split(':');

                        years.push(parseInt(date[0]));
                        months.push(parseInt(date[1]));
                        days.push(parseInt(date[2]));
                        hours.push(time[0]);
                        minutes.push(time[1]);
                    }

                    var formatter = new google.visualization.NumberFormat({
                        fractionDigits: 10
                    });

                    /** 그래프에 표시할 데이터 */
                    let arr = new Array();

                    for (let i = 0; i < symbols.length; i++) {
                        let symbol = coin[symbols[i]]
                        for (let j = 0; j < symbol.length; j++) {

                            let price = parseFloat(symbol[j].price);

                            if (arr[j] == null) {
                                arr[j] = [
                                    new Date(years[j], months[j] - 1, days[j], hours[j], minutes[j]),
                                ]
                            }

                            arr[j].push({v: price, f: price.toFixed(10)});
                        }
                    }

                    data.addRows(arr)

                    formatter.format(data, 1);

                    var control = new google.visualization.ControlWrapper({
                        controlType: 'ChartRangeFilter',
                        containerId: 'filter_div',
                        options: {
                            ui: {
                                chartType: 'LineChart',
                                chartOptions: {
                                    chartArea: {'width': '60%', 'height': 80},
                                }
                            },
                            filterColumnIndex: 0
                        }
                    });

                    var chart = new google.visualization.ChartWrapper({
                        chartType: 'LineChart',
                        containerId: 'chart_div',
                        options: {
                            title: `1USD : ${exchanges.get("KRW")} KRW, ${exchanges.get("JPY")} JPY`,
                            width: 800,
                            height: 500,
                            hAxis: {
                                // format: 'MM-dd HH:mm'
                                format: 'HH:mm'
                            },
                            vAxis: {
                                format: 'decimal',
                            },
                            animation: {duration: 500, easing: 'in'},
                            chartArea: {
                                bottom: 20,
                                top: 30,
                                width: "75%",
                                height: "100%"
                            },
                        }
                    });

                    var dashboard = new google.visualization.Dashboard(document.getElementById('dashboard_div'));
                    window.addEventListener('resize', function () {
                        dashboard.draw(data);
                    }, false);
                    dashboard.bind([control], [chart]);
                    dashboard.draw(data);
                }
            })
        }
    </script>
</head>
<body>
<div class="container">
    <div class="row">
        <div id="dashboard_div" class="col">
            <div id="chart_div"></div>
            <div id="filter_div"></div>
        </div>
        <div class="col">
            <table class="thead">
                <colgroup>
                    <col width="33%">
                    <col width="33%">
                    <col width="33%">
                </colgroup>
                <thead>
                <th class="exchange select_exchange">USD</th>
                <th class="exchange">KRW</th>
                <th class="exchange">JPY</th>
                <script>
                    $('.exchange').on("click", function () {
                        $('.exchange').removeClass('select_exchange');
                        $(this).addClass('select_exchange');

                        const exchange = $(this).text();
                        if (exchange == "USD") {
                            for (let i = 0; i < $('.price').get().length; i++) {
                                $('.price').eq(i).text(${coins}[i].price);
                            }
                        } else if (exchange == "KRW") {
                            for (let i = 0; i < $('.price').get().length; i++) {
                                const KRW = ${coins}[i].price * ${exchanges.get("KRW") / 1000};
                                $('.price').eq(i).text(KRW);
                            }
                        } else if (exchange == "JPY") {
                            for (let i = 0; i < $('.price').get().length; i++) {
                                const JPY = ${coins}[i].price * ${exchanges.get("JPY") / 100};
                                $('.price').eq(i).text(JPY);
                            }
                        }
                    })
                </script>
                </thead>
            </table>
            <div class="list_container">
                <table>
                    <colgroup>
                        <col width="40%"/>
                        <col width="60%"/>
                    </colgroup>
                    <tbody class="list">

                    </tbody>
                    <script type="text/javascript">
                        $.each(${coins}, function (i, coin) {
                            let row = $("<tr class='ticker'></tr>");
                            let symbol = $("<td class='symbol'>" + coin.symbol + "</td>");
                            let price = $("<td class='price'>" + coin.price + "</td>");
                            row.append(symbol).append(price);

                            $('.list').append(row);
                        })

                        $('.ticker:eq(0)').addClass("select_ticker");

                        $('.ticker').on("click", function () {
                            $(this).toggleClass('select_ticker');

                            drawDashboard();
                        });
                    </script>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
