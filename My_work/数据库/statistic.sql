--地域数量分布，降序
SELECT  "国标地域代码" , count (*) from rawstat."Table_县2000" GROUP BY "国标地域代码"  ORDER BY count (*) DESC
--包含“云南”这个关键词的分类数目
SELECT count("数值来源") FROM "public"."Table_县2000" WHERE "数值来源" like '%云南%' GROUP BY "数值来源"
--包含“云南”这个关键词的所有数目
SELECT count("数值来源") FROM "public"."Table_县2000" WHERE "数值来源" like '%云南%' 
--数值来源包含“云南“和且正式指标包含”工业”这两个关键词的搜索结果
SELECT T .* FROM "public"."Table_县2000" T,(SELECT "正式指标","数值来源" FROM "public"."Table_县2000" WHERE "正式指标" LIKE '%工业%' AND "数值来源" LIKE '%云南%') t1
WHERE T ."正式指标" = t1."正式指标" AND T ."数值来源" = t1."数值来源"
--查询所有“正式指标”情况
SELECT T.* FROM "public"."Table_县2000" as T, ( SELECT "正式指标" from "public"."Table_县2000" GROUP BY "正式指标") t1 WHERE T."正式指标" = t1."正式指标"
--查询所有“数值来源”情况
SELECT T.* FROM "public"."Table_县2000" as T, ( SELECT "数值来源" from "public"."Table_县2000" GROUP BY "数值来源") t1 WHERE T."数值来源" = t1."数值来源"
--在“数值来源”列查询包含“云南”和“工业”两关键词的所有内容
SELECT * FROM "public"."Table_县2000" where "数值来源" LIKE '%云南%工业%'
--查询重复的部分
select  "年份","国标地域代码","正式指标","显示数值","显示单位","数值来源" from rawstat."Table_市2010" 
INTERSECT
select "年份","国标地域代码","正式指标","显示数值","显示单位","数值来源" from rawstat."Table_市2010_new"
--两表无重复合并
select  "年份","国标地域代码","正式指标","显示数值","显示单位","数值来源" from rawstat."Table_市2010" 
union
select "年份","国标地域代码","正式指标","显示数值","显示单位","数值来源" from rawstat."Table_市2010_new"
--查询表数据总数
select  count("年份") from rawstat."Table_市2010"  GROUP BY "年份"
--单表去重
select  "年份","国标地域代码","正式指标","显示数值","显示单位","数值来源" from rawstat."Table_市2010_new" group by  "年份","国标地域代码","正式指标","显示数值","显示单位","数值来源"
--查空
select * from rawstat."Table_市2010" where "国标地域代码" is NULL or "年份" is NULL or "正式指标" is NULL or "显示数值" is null or "显示单位" is null or "数值来源" is null or "标示" is null
--地域代码与地域名称匹配
select t1."年份", t1."国标地", t2."地域名称", t1."正式指", t1."显示数", t1."显示单", t1."数值来" from "2010_table_shi" t1, a t2 WHERE t1."国标地"= t2."地域代码"
