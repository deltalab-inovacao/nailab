-- lista de projetos
SELECT nh.id, nh.name
FROM testlink.nodes_hierarchy nh
left join testlink.node_types nt on nh.node_type_id=nt.id 
where nt.description = "testproject"

-- lista de caso de teste
-- parametro PROJECT_ID
-- SET @PROJECT_ID := 17512;
select nh.name project, test_case.id test_case_id, test_case.name, CONCAT(nh.name ,'/', nh2.name ,'/', nh1.name) path
from (
	select  id,
	        name,
	        parent_id, description
	from    (select nh.*, nt.description from testlink.nodes_hierarchy nh
	left join testlink.node_types nt on nh.node_type_id=nt.id 
	         order by parent_id, id) nh,
	        (select @pv := @PROJECT_ID) initialisation
	where   find_in_set(parent_id, @pv)
	and     length(@pv := concat(@pv, ',', id))
	
) test_case
left join testlink.nodes_hierarchy nh1 on test_case.parent_id = nh1.id
left join testlink.nodes_hierarchy nh2 on nh1.parent_id = nh2.id
left join testlink.nodes_hierarchy nh3 on nh2.parent_id = nh3.id
left join testlink.nodes_hierarchy nh on nh.id = @PROJECT_ID
where  test_case.description = 'testcase' 


-- detalhes de caso de teste
-- Parametro TEST_CASE_ID
-- SET @TEST_CASE_ID := 29690;
SELECT tcv.id, nh2.name, tc_external_id, version, layout, status, summary, preconditions, 
importance, author_id, creation_ts, updater_id, modification_ts, active, is_open, execution_type, estimated_exec_duration
FROM testlink.tcversions tcv
left join testlink.nodes_hierarchy nh on nh.id = tcv.id
left join testlink.nodes_hierarchy nh2 on nh.parent_id = nh2.id
where nh2.id = @TEST_CASE_ID
;

-- Passos do caso de teste
-- Parametro TEST_CASE_ID
-- SET @TEST_CASE_ID := 29690;
SELECT tcs.id step_id, tcs.step_number, tcs.actions, tcs.expected_results, 
nh.name step_name, 
nh2.id testcase_version_id, nh2.name testcase_version_name,
nh3.id testcase_id, nh3.name testcase_name
FROM testlink.tcsteps tcs
left join  testlink.nodes_hierarchy nh on tcs.id = nh.id  
left join testlink.nodes_hierarchy nh2 on nh.parent_id = nh2.id
left join testlink.nodes_hierarchy nh3 on nh2.parent_id = nh3.id
left join testlink.node_types nt2 on nh2.node_type_id=nt2.id
left join testlink.node_types nt3 on nh3.node_type_id=nt3.id
where nh3.id = @TEST_CASE_ID;

