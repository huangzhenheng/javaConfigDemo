<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>济宁市网格化社会治理信息云图</title>
    <link rel="stylesheet" href="/static/jnbs/css/reset-1.3.0.css">
    <link rel="stylesheet" href="/static/jnbs/css/index.css">
</head>

<body>
    
    <div class="page-index-jn" id="jn">
        <!-- 头部 -->
        <div class="header">
            <div></div>
        </div>
        <!-- 主体 -->
        <div class="wrap">
            <div class="left">
                <div class="left-top mb-20">
                    <div class="nav-title">
                        <div class="nav-title-left">
                            <span class="nav-name">组织机构</span>
                            <br/>
                            <span class="english">organization</span>
                        </div>
                        <div class="nav-title-right">
                            <div class="nav-count">\01</div>
                        </div>
                    </div>
                    <div class="base-content">
                        <div class="info">
                            <div>
                                网络员共:&nbsp;&nbsp;2000人
                            </div>
                            <div>
                                在线人数:&nbsp;&nbsp;2000人
                            </div>
                            <div>
                                综治机构:&nbsp;&nbsp;2000人
                            </div>
                            <div>
                                机构成员:&nbsp;&nbsp;2000人
                            </div>
                            <div>
                                服务团队:&nbsp;&nbsp;2000人
                            </div>
                            <div>
                                服务成员:&nbsp;&nbsp;2000人
                            </div>
                        </div>
                    </div>
                    
                </div>
                <div class="left-middle mb-20">
                    <div class="nav-title">
                        <div class="nav-title-left">
                            <span class="nav-name">实有房屋</span>
                            <br/>
                            <span class="english">Real house</span>
                        </div>
                        <div class="nav-title-right">
                            <div class="nav-count">\02</div>
                        </div>
                    </div>
                    <div class="base-content-special">
                        <div class="info-special">
                            <div class="room-content">
                                <div>
                                    <span>实有房屋</span>
                                    <p>1111间</p>
                                </div>
                                <div>
                                    <span>居住人数</span>
                                    <p>8900人</p>
                                </div>
                                </div>
                                <div class="room-content">
                                <div>
                                    <span>出租房共</span>
                                    <p>90间</p>
                                </div>
                                <div>
                                    <span>居住人数</span>
                                    <p>200人</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="left-bottom">
                    <div class="nav-title">
                        <div class="nav-title-left">
                            <span class="nav-name">实有人口</span>
                            <br/>
                            <span class="english">Real population</span>
                        </div>
                        <div class="nav-title-right">
                            <div class="nav-count">\03</div>
                        </div>
                    </div>
                    <div class="base-content-chart">
                        <div class="info-1">
                            <div class="pie-chart" id="pie-chart"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="main">
                <div class="map-wrap">
                    <div id="jnMap" class="jnMap"></div>
                    <div class="goBack"><a>返回</a></div>
                </div>
                <div class="record">
                    <div class="top">
                        日志统计
                        <br>
                        <span class="english">Log Statistics</span>
                    </div>
                    <div class="record-content">
                        <div class="manRecord">
                            <div class="num-l">199</div>
                        </div>
                        <div class="monthAdd">
                            <div class="num-r">200</div>
                        </div>
                    </div>
                    <div class="record-content">
                        <div class="workRecord">
                            <div class="num-l">2000</div>
                        </div>
                        <div class="monthAdd">
                            <div class="num-r">99</div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="right">
                <div class="right-top mb-20">
                    <div class="nav-title">
                        <div class="nav-title-left">
                            <span class="nav-name">事件处理(本月)</span>
                            <br/>
                            <span class="english">Event Processing (this month)</span>
                        </div>
                        <div class="nav-title-right">
                            <div class="nav-count">\04</div>
                        </div>
                    </div>
                    <div class="base-content-chart top-80">
                        <div class="info-1">
                            <div class="bar-chart" id="bar-chart"></div>
                        </div>
                    </div>
                </div>
                <div class="right-middle mb-20">
                    <div class="nav-title">
                        <div class="nav-title-left">
                            <span class="nav-name">事件类型统计</span>
                            <br/>
                            <span class="english">Event Type Statistics</span>
                        </div>
                        <div class="nav-title-right">
                            <div class="nav-count">\05</div>
                        </div>
                    </div>
                    <div class="base-content-chart">
                        <div class="info-1">
                            <div class="pie-chart-two" id="pie-chart-two"></div>
                        </div>
                    </div>
                </div>
                <div class="right-bottom mb-20">
                    <div class="nav-title">
                        <div class="nav-title-left">
                            <span class="nav-name">当月事件区域分布</span>
                            <br/>
                            <span class="english">Month event area distribution</span>
                        </div>
                        <div class="nav-title-right">
                            <div class="nav-count">\06</div>
                        </div>
                    </div>
                    <div class="events">
                        <div class="events-title clr">
                            <span>区域</span>
                            <span>占比</span>
                            <span>较多类别</span>
                            <span>趋势</span>
                        </div>
                        <div class="events-list">
                            <ul>
                                <li>
                                    <a href="javascript:;">
                                        <span>区域</span>
                                        <span>占比</span>
                                        <span>较多类别</span>
                                        <span>
                                            <i class="up"></i>
                                        </span>
                                    </a>
                                </li>
                                <li>
                                    <a href="javascript:;">
                                        <span>区域</span>
                                        <span>占比</span>
                                        <span>较多类别</span>
                                        <span>
                                            <i class="down"></i>
                                        </span>
                                    </a>
                                </li>
                                <li>
                                    <a href="javascript:;">
                                        <span>区域</span>
                                        <span>占比</span>
                                        <span>较多类别</span>
                                        <span>
                                            <i class="up"></i>
                                        </span>
                                    </a>
                                </li>
                                <li>
                                    <a href="javascript:;">
                                        <span>区域</span>
                                        <span>占比</span>
                                        <span>较多类别</span>
                                        <span>
                                            <i class="up"></i>
                                        </span>
                                    </a>
                                </li>
                                <li>
                                    <a href="javascript:;">
                                        <span>区域</span>
                                        <span>占比</span>
                                        <span>较多类别</span>
                                        <span>
                                            <i class="down"></i>
                                        </span>
                                    </a>
                                </li>
                                <li>
                                    <a href="javascript:;">
                                        <span>区域</span>
                                        <span>占比</span>
                                        <span>较多类别</span>
                                        <span>
                                            <i class="down"></i>
                                        </span>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
<script src="/static/jnbs/js/jquery-1.4.2.min.js"></script>
<script src="/static/jnbs/js/jquery.SuperSlide.2.1.1.js"></script>
<script src="/static/jnbs/js/lodash.js"></script>
<!-- <script src="https://cdn.bootcss.com/lodash.js/4.17.5/lodash.js"></script> -->
<script src="/static/jnbs/js/echarts.min.js"></script>
<script src="/static/jnbs/js/cmap.js"></script>
<script src="/static/jnbs/js/mapCity.js"></script>
<script src="/static/jnbs/js/index.js"></script>

</html>