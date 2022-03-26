INSERT INTO rbp_job.xxl_job_info
(id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
VALUES(4, 5, '拉取APP商品列表', '2022-01-10 15:50:26', '2022-01-10 16:44:44', 'T', '', 'NONE', '', 'DO_NOTHING', 'FIRST', 'inno.downloadOnlineGoodsListJobHandler', '{ "onlinePlatformCode": "INNO" }', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2022-01-10 15:50:26', '', 0, 0, 0);
INSERT INTO rbp_job.xxl_job_info
(id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
VALUES(5, 5, '同步渠道', '2022-01-10 16:45:30', '2022-01-10 16:45:30', 'T', '', 'NONE', '', 'DO_NOTHING', 'FIRST', 'inno.PostErpStore', '{ "onlinePlatformCode": "INNO" }', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2022-01-10 16:45:30', '', 0, 0, 0);
INSERT INTO rbp_job.xxl_job_info
(id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
VALUES(6, 5, '拉取优惠券类型列表', '2022-01-10 16:45:58', '2022-01-10 16:45:58', 'T', '', 'NONE', '', 'DO_NOTHING', 'FIRST', 'inno.GetCouponPolicy', '{ "onlinePlatformCode": "INNO" }', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2022-01-10 16:45:58', '', 0, 0, 0);
INSERT INTO rbp_job.xxl_job_info
(id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
VALUES(7, 5, '同步员工档案', '2022-01-10 16:46:32', '2022-01-10 16:46:32', 'T', '', 'NONE', '', 'DO_NOTHING', 'FIRST', 'inno.PostErpEmployee', '{ "onlinePlatformCode": "INNO" }', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2022-01-10 16:46:32', '', 0, 0, 0);
INSERT INTO rbp_job.xxl_job_info
(id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
VALUES(8, 5, '上传会员', '2022-01-10 16:47:01', '2022-01-10 16:47:01', 'T', '', 'NONE', '', 'DO_NOTHING', 'FIRST', 'inno.PostErpUsers', '{ "onlinePlatformCode": "INNO" }', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2022-01-10 16:47:01', '', 0, 0, 0);
INSERT INTO rbp_job.xxl_job_info
(id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
VALUES(9, 5, '全量更新上传inno仓库库存', '2022-01-10 16:47:35', '2022-01-10 16:47:35', 'T', '', 'NONE', '', 'DO_NOTHING', 'FIRST', 'inno.onlineSyncGoodsStockFullJobHandler', '{ "onlinePlatformCode": "INNO"}', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2022-01-10 16:47:35', '', 0, 0, 0);
INSERT INTO rbp_job.xxl_job_info
(id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
VALUES(10, 5, '定时更新上传inno仓库库存', '2022-01-10 16:48:09', '2022-01-10 16:48:09', 'T', '', 'NONE', '', 'DO_NOTHING', 'FIRST', 'inno.onlineSyncGoodsStockJobHandler', '{ "onlinePlatformCode": "INNO"}', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2022-01-10 16:48:09', '', 0, 0, 0);
INSERT INTO rbp_job.xxl_job_info
(id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
VALUES(11, 5, '拉取订单列表', '2022-01-10 16:49:21', '2022-01-10 16:49:21', 'T', '', 'NONE', '', 'DO_NOTHING', 'FIRST', 'inno.downloadOnlineOrderListJobHandler', '{ "onlinePlatformCode": "INNO"}', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2022-01-10 16:49:21', '', 0, 0, 0);
INSERT INTO rbp_job.xxl_job_info
(id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
VALUES(12, 5, '更新退货单收货状态', '2022-01-10 16:49:50', '2022-01-10 16:49:50', 'T', '', 'NONE', '', 'DO_NOTHING', 'FIRST', 'inno.PostUpdateReturnOrderStatus', '{ "onlinePlatformCode": "INNO" }', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2022-01-10 16:49:50', '', 0, 0, 0);
INSERT INTO rbp_job.xxl_job_info
(id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
VALUES(13, 5, '拉取全渠道退货通知单列表', '2022-01-10 16:51:00', '2022-01-10 16:51:00', 'T', '', 'NONE', '', 'DO_NOTHING', 'FIRST', 'inno.GetAppReturnOrder', '{ "onlinePlatformCode": "INNO", "orderSn": "", "returnSn": "THD000532110141327194170111" }', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2022-01-10 16:51:00', '', 0, 0, 0);
INSERT INTO rbp_job.xxl_job_info
(id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
VALUES(14, 5, '自动数据库删除临时表', '2022-01-10 16:52:06', '2022-01-10 16:52:06', 'T', '', 'NONE', '', 'DO_NOTHING', 'FIRST', 'autoDeleteTempTable', '{ "action": "default"}', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2022-01-10 16:52:06', '', 0, 0, 0);
INSERT INTO rbp_job.xxl_job_info
(id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
VALUES(15, 5, '自动完结销售计划', '2022-01-10 16:52:45', '2022-01-10 16:52:45', 'T', '', 'NONE', '', 'DO_NOTHING', 'FIRST', 'salePlanBillAutoComplete', '{ "billNo": "" }', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2022-01-10 16:52:45', '', 0, 0, 0);
INSERT INTO rbp_job.xxl_job_info
(id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
VALUES(16, 5, '自动完结指令单', '2022-01-10 16:53:05', '2022-01-10 16:53:05', 'T', '', 'NONE', '', 'DO_NOTHING', 'FIRST', 'noticeBillAutoComplete', '{ "billNo": "" }', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2022-01-10 16:53:05', '', 0, 0, 0);
INSERT INTO rbp_job.xxl_job_info
(id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
VALUES(17, 5, '自动采购单', '2022-01-10 16:53:25', '2022-01-10 16:53:25', 'T', '', 'NONE', '', 'DO_NOTHING', 'FIRST', 'purchaseBillAutoComplete', '{ "billNo": "" }', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2022-01-10 16:53:25', '', 0, 0, 0);
INSERT INTO rbp_job.xxl_job_info
(id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
VALUES(18, 5, '下载会员', '2022-01-10 16:54:21', '2022-01-10 16:54:21', 'T', '', 'NONE', '', 'DO_NOTHING', 'FIRST', 'inno.GetUserList', '{ "onlinePlatformCode": "INNO" }', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2022-01-10 16:54:21', '', 0, 0, 0);
