
--删除面
DROP    FUNCTION IF EXISTS CT_DeleteSurfaceWKT(id varchar) ;
CREATE OR REPLACE FUNCTION CT_DeleteSurfaceWKT(id varchar) RETURNS void AS $$
    DECLARE
    BEGIN
        DELETE FROM e_grid_surface WHERE surface_id = id;
        DELETE FROM e_surface_surface WHERE surface_id1 = id or surface_id2 = id;
        DELETE FROM e_point_surface WHERE surface_id = id;
        DELETE FROM e_line_surface WHERE surface_id = id;
        DELETE FROM e_surface WHERE surface_id = id;
        DELETE FROM e_surface_piece WHERE surface_id = id;
    END;
$$ LANGUAGE plpgsql;



--面关系 wkt
--DROP    FUNCTION IF EXISTS CT_SurfaceRelateWKT(id varchar, geotext text, epsg integer);
--CREATE OR REPLACE FUNCTION CT_SurfaceRelateWKT(id varchar, geotext text, epsg integer) RETURNS void AS $$
--    DECLARE
--        g4326 geometry;
--    BEGIN
--	    g4326 := ST_Transform(ST_GeomFromText(geotext,epsg),4326); 
--        INSERT INTO e_point_surface(point_id, surface_id) SELECT e_point.point_id, id FROM e_point WHERE ST_Intersects(e_point.the_geom, g4326);
--        INSERT INTO e_line_surface(line_id, surface_id, length) SELECT e_line.line_id, id, ST_Length(Geography(ST_Intersection(e_line.the_geom, g4326))) FROM e_line WHERE ST_Intersects(e_line.the_geom, g4326);
--        INSERT INTO e_grid_surface(surface_id, grid_id, area ) SELECT id, e_grid.grid_id, ST_Area(Geography(ST_Intersection(e_grid.the_geom, g4326))) FROM e_grid WHERE ST_Intersects(e_grid.the_geom, g4326); 
        --INSERT INTO e_surface_surface(surface_id1, surface_id2, area ) SELECT id, e_surface.surface_id, ST_Area(Geography(ST_Intersection(e_surface.the_geom, g4326))) FROM e_surface WHERE ST_Intersects(e_surface.the_geom, g4326) and e_surface.kind <> 'U'; 

--    END;
--$$ LANGUAGE plpgsql;






--初始添加面 wkt 有id
DROP FUNCTION IF EXISTS    CT_InsertSurfaceWKT(vid varchar, geotext text, epsg integer, vname varchar, vuser_id varchar, vkind varchar, parent varchar );
CREATE OR REPLACE FUNCTION CT_InsertSurfaceWKT(vid varchar, geotext text, epsg integer, vname varchar, vuser_id varchar, vkind varchar, parent varchar ) RETURNS void AS $$
    DECLARE
        gInput geometry;
        g4326 geometry;
        cent geometry;
        simple geometry;
    BEGIN
        gInput := ST_MakeValid(ST_GeomFromText(geotext,epsg));
        IF epsg = 4326 THEN
            g4326 := gInput;
        ELSE
            g4326 := ST_Transform(gInput,4326);   
        END IF;
        simple := st_multi( st_simplifypreservetopology(g4326, 0.2) );
        cent :=   ST_Transform(ST_Centroid(g4326),3857);

        INSERT INTO e_surface (surface_id, the_geom, simple_geom, area, weight_x, weight_y,                name, user_id,  kind,   parent_surface_id) 
                       VALUES (vid, g4326, simple, ST_Area(Geography(g4326)), ST_X(cent), ST_Y(cent), vname, vuser_id, vkind, parent);
        INSERT INTO e_surface_surface_group (surface_group_id, surface_id) VALUES (vkind, vid);
        IF st_npoints(g4326) > 20 THEN
             PERFORM  CT_CutSurfacePiece(vid , g4326, vkind);
        ELSE
             INSERT INTO e_surface_piece (surface_id, grid_id, level, the_geom, kind) VALUES (vid, '0', 0, g4326, vkind);
        END IF;
        INSERT INTO e_point_surface(point_id, surface_id ) SELECT e_point.point_id, vid FROM e_point WHERE ST_Intersects(e_point.the_geom, g4326);
        --INSERT INTO e_line_surface(line_id, surface_id, length) SELECT e_line.line_id, vid,  ST_Length(Geography(ST_Intersection(e_line.the_geom, g4326))) FROM e_line WHERE ST_Intersects(e_line.the_geom, g4326);
        INSERT INTO e_line_surface(line_id, surface_id, length)( SELECT e_line.line_id, vid,  sum(ST_Length(Geography(ST_Intersection(e_line.the_geom, e_surface_piece.the_geom)))) FROM e_line, e_surface_piece WHERE ST_Intersects(e_line.the_geom, e_surface_piece.the_geom) AND e_surface_piece.surface_id = vid GROUP BY e_line.line_id);
--        INSERT INTO e_grid_surface(grid_id, surface_id, area ) SELECT  e_grid.grid_id, vid,  ST_Area(Geography(ST_Intersection(e_grid.the_geom, g4326))) FROM e_grid WHERE ST_Intersects(e_grid.the_geom, g4326); 
        INSERT INTO e_grid_surface(grid_id, surface_id,  area )( SELECT e_grid.grid_id, vid,  sum( ST_Area(Geography(ST_Intersection(e_grid.the_geom, e_surface_piece.the_geom)))) FROM e_grid, e_surface_piece  WHERE ST_Intersects(e_grid.the_geom, e_surface_piece.the_geom) AND e_surface_piece.surface_id = vid GROUP BY e_grid.grid_id); 
  
      --INSERT INTO e_surface_surface(surface_id1, surface_id2) SELECT vid, e_surface.surface_id  FROM e_surface WHERE ST_Intersects(e_surface.the_geom, g4326) and e_surface.kind = 'U'; 

    END;
$$ LANGUAGE plpgsql;



--初始添加面 wkt 无id
DROP FUNCTION IF EXISTS    CT_InsertSurfaceWKT( geotext text, epsg integer, vname varchar, vuser_id varchar, vkind varchar, parent varchar );
CREATE OR REPLACE FUNCTION CT_InsertSurfaceWKT( geotext text, epsg integer, vname varchar, vuser_id varchar, vkind varchar, parent varchar ) RETURNS void AS $$
    DECLARE
        uuid varchar;
    BEGIN
        uuid = uuid_generate_v1();
        PERFORM CT_InsertSurfaceWKT(uuid, geotext, epsg, vname, vuser_id, vkind, parent );
    END;
$$ LANGUAGE plpgsql;




--更新面 wkt
DROP FUNCTION IF EXISTS    CT_UpdateSurfaceWKT(vid varchar, geotext text, epsg integer, vkind varchar);
CREATE OR REPLACE FUNCTION CT_UpdateSurfaceWKT(vid varchar, geotext text, epsg integer, vkind varchar) RETURNS void AS $$
    DECLARE
        gInput geometry;
        g4326 geometry;
        cent geometry;
        simple geometry;
    BEGIN
        gInput := ST_MakeValid(ST_GeomFromText(geotext,epsg));
        IF epsg = 4326 THEN
            g4326 := gInput;
        ELSE
            g4326 := ST_Transform(gInput,4326);   
        END IF;
        simple := st_multi( st_simplifypreservetopology(g4326, 0.2) );

        cent := ST_Transform(ST_Centroid(g4326),3857);

        DELETE FROM e_grid_surface WHERE surface_id = vid;
        DELETE FROM e_point_surface WHERE surface_id = vid;
        DELETE FROM e_line_surface WHERE surface_id = vid;
        DELETE FROM e_surface_piece WHERE surface_id = vid;
        UPDATE e_surface SET simple_geom = simple, the_geom = g4326, weight_x = ST_X(cent), weight_y = ST_Y(cent), area = ST_Area(Geography(g4326))  WHERE surface_id = vid;
        IF st_npoints(g4326) > 20 THEN
             PERFORM  CT_CutSurfacePiece(vid , g4326, vkind);
        ELSE
             INSERT INTO e_surface_piece (surface_id, grid_id, level, the_geom, kind) VALUES (vid, '0', 0, g4326, vkind);
        END IF;




        INSERT INTO e_point_surface(point_id, surface_id ) SELECT e_point.point_id, vid FROM e_point WHERE ST_Intersects(e_point.the_geom, g4326);
        --INSERT INTO e_line_surface(line_id, surface_id, length) SELECT e_line.line_id, vid,  ST_Length(Geography(ST_Intersection(e_line.the_geom, g4326))) FROM e_line WHERE ST_Intersects(e_line.the_geom, g4326);
        INSERT INTO e_line_surface(line_id, surface_id, length)( SELECT e_line_piece.line_id, vid,  sum(ST_Length(Geography(ST_Intersection(e_line_piece.the_geom, e_surface_piece.the_geom)))) FROM e_line_piece, e_surface_piece WHERE ST_Intersects(e_line_piece.the_geom, e_surface_piece.the_geom) AND e_surface_piece.surface_id = vid GROUP BY e_line_piece.line_id);
 --        INSERT INTO e_grid_surface(grid_id, surface_id, area ) SELECT  e_grid.grid_id, vid,  ST_Area(Geography(ST_Intersection(e_grid.the_geom, g4326))) FROM e_grid WHERE ST_Intersects(e_grid.the_geom, g4326); 
        INSERT INTO e_grid_surface(grid_id, surface_id,  area )( SELECT e_grid.grid_id, vid,  sum(ST_Area(Geography(ST_Intersection(e_grid.the_geom, e_surface_piece.the_geom)))) FROM e_grid, e_surface_piece  WHERE ST_Intersects(e_grid.the_geom, e_surface_piece.the_geom) AND e_surface_piece.surface_id = vid GROUP BY e_grid.grid_id); 
       --INSERT INTO e_surface_surface(surface_id1, surface_id2) SELECT e_surface.surface_id, vid FROM e_surface WHERE ST_Intersects(e_surface.the_geom, g4326) and e_surface.kind <> 'U'; 
        --surface_id1是行政 surface_id2是用户
    END;
$$ LANGUAGE plpgsql;




--查询面 wkt
DROP FUNCTION IF EXISTS    CT_SelectSurfaceWKT(id varchar, epsg integer );
CREATE OR REPLACE FUNCTION CT_SelectSurfaceWKT(id varchar, epsg integer ) RETURNS text AS $$

    DECLARE
        gepsg geometry;
    BEGIN
	    SELECT INTO gepsg ST_Transform(the_geom, epsg) FROM e_surface WHERE surface_id = id;
        RETURN ST_AsText(gepsg);       
    END;
$$ LANGUAGE plpgsql;






---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------




--初始添加面 wkt 有id

--CREATE OR REPLACE FUNCTION CT_CutSurfacePiece(vid varchar, geotext text, epsg integer, vname varchar, vuser_id varchar, vkind varchar,  parent varchar ) RETURNS void AS $$
--    DECLARE
--        gInput geometry;
--        g4326 geometry;
--        cent geometry;
--        kernal  geometry;
--    BEGIN
--        gInput := ST_GeomFromText(geotext,epsg);
--        IF epsg = 4326 THEN
--            g4326 := gInput;
--        ELSE
--            g4326 := ST_Transform(gInput,4326);   
--        END IF;
--        IF epsg = 3857 THEN
--            cent :=  ST_Centroid(gInput);
--        ELSE
--            cent :=  ST_Transform(ST_Centroid(gInput),3857);  
--        END IF;
--
--        kernal := st_multi(  st_simplifypreservetopology(st_buffer( st_simplifypreservetopology(g4326, 0.2) ,-0.45, 'quad_segs=2'),0.25) );


--        INSERT INTO e_surface (surface_id, the_geom, area,                      weight_x,   weight_y,   name,  user_id,  kind,   parent_surface_id ) 
--                       VALUES (vid,         g4326,    ST_Area(Geography(g4326)), ST_X(cent), ST_Y(cent), vname, vuser_id, vkind,  parent) ;
--        INSERT INTO e_surface_surface_group (surface_group_id, surface_id)
--                                     VALUES (vkind,            vid);
        
        --insert kernal
--        INSERT INTO e_surface_piece (type, surface_id, grid_id, the_geom) VALUES( 'k', vid, uuid_generate_v1(), ST_Multi(kernal)    ) ;
--        INSERT INTO e_surface_piece (type, surface_id, grid_id, the_geom) 
--        SELECT 'i', vid, gid, ST_Multi(interdiff) From (SELECT  e_base_grid.id as gid, ST_Intersection(ST_Difference(e_base_grid.the_geom,kernal),g4326) as interdiff FROM e_base_grid WHERE NOT ST_Within(e_base_grid.the_geom,kernal)  AND ST_Intersects(e_base_grid.the_geom, g4326) ) as tab1  WHERE  GeometryType(interdiff) IN('POLYGON','MULTIPOLYGON') ;


        --INSERT INTO e_surface_piece (type, surface_id, grid_id, the_geom) SELECT 'i', vid, e_base_grid.id, ST_Multi(ST_Intersection(ST_Difference(e_base_grid.the_geom, kernal), g4326)) FROM e_base_grid WHERE ST_Intersects(ST_Difference(e_base_grid.the_geom,kernal), g4326) AND GeometryType(ST_Intersection(ST_Difference(e_base_grid.the_geom,kernal), g4326)) IN('POLYGON','MULTIPOLYGON')       ;
                            
        --PERFORM CT_SurfaceRelateWKT(id, geotext, epsg);
        --INSERT INTO e_point_surface(point_id, surface_id ) SELECT e_point.point_id, id FROM e_point WHERE ST_Intersects(e_point.the_geom, g4326);
        --INSERT INTO e_line_surface(line_id, surface_id, length) SELECT e_line.line_id, id,  ST_Length(Geography(ST_Intersection(e_line.the_geom, g4326))) FROM e_line WHERE ST_Intersects(e_line.the_geom, g4326);
        --INSERT INTO e_grid_surface(surface_id, grid_id,  area ) SELECT id, e_grid.grid_id,  ST_Area(Geography(ST_Intersection(e_grid.the_geom, g4326))) FROM e_grid WHERE ST_Intersects(e_grid.the_geom, g4326); 
        --INSERT INTO e_surface_surface(surface_id1, surface_id2) SELECT id, e_surface.surface_id  FROM e_surface WHERE ST_Intersects(e_surface.the_geom, g4326) and e_surface.kind = 'U'; 

--    END;
--$$ LANGUAGE plpgsql;






----------------------------------------------------------------------------------------------------------------------------------------------------------------------




DROP FUNCTION IF EXISTS    CT_CutSurfacePiece(vid varchar, g4326 geometry, vkind varchar );
CREATE OR REPLACE FUNCTION CT_CutSurfacePiece(vid varchar, g4326 geometry, vkind varchar ) RETURNS void AS $$
    DECLARE
        rec RECORD;
        recc RECORD;
        reccc RECORD;
    BEGIN

        FOR rec IN SELECT e_base_grid_1.id as gid,  ST_Multi(ST_Intersection(e_base_grid_1.the_geom, g4326)) as the_geom FROM e_base_grid_1 
                         WHERE ST_Intersects(e_base_grid_1.the_geom, g4326) AND GeometryType(ST_Intersection(e_base_grid_1.the_geom, g4326)) IN('POLYGON','MULTIPOLYGON') 
        LOOP
            IF st_npoints(rec.the_geom) < 20 THEN
                 INSERT INTO e_surface_piece (surface_id, grid_id, level, the_geom, kind) VALUES (vid, rec.gid, 1, rec.the_geom, vkind);
            ELSE
                 FOR recc IN SELECT e_base_grid_2.id as gid,  ST_Multi(ST_Intersection(e_base_grid_2.the_geom, rec.the_geom)) as the_geom FROM e_base_grid_2 
                                    WHERE ST_Intersects(e_base_grid_2.the_geom, rec.the_geom) AND GeometryType(ST_Intersection(e_base_grid_2.the_geom, rec.the_geom)) IN('POLYGON','MULTIPOLYGON') 
                 LOOP
                    IF st_npoints(recc.the_geom) < 20 THEN
                        INSERT INTO e_surface_piece (surface_id, grid_id, level, the_geom, kind) VALUES (vid, recc.gid, 2, recc.the_geom, vkind);
                    ELSE
                         FOR reccc IN SELECT e_base_grid_3.id as gid,  ST_Multi(ST_Intersection(e_base_grid_3.the_geom, recc.the_geom)) as the_geom FROM e_base_grid_3 
                                            WHERE ST_Intersects(e_base_grid_3.the_geom, recc.the_geom) AND GeometryType(ST_Intersection(e_base_grid_3.the_geom, recc.the_geom)) IN('POLYGON','MULTIPOLYGON') 
                         LOOP
                             IF st_npoints(reccc.the_geom) < 20 THEN
                                  INSERT INTO e_surface_piece (surface_id, grid_id, level, the_geom, kind) VALUES (vid, reccc.gid, 3, reccc.the_geom, vkind);
                             ELSE
                                  INSERT INTO e_surface_piece (surface_id, grid_id, level, the_geom, kind) SELECT vid, e_base_grid_4.id, 4, ST_Multi(ST_Intersection(e_base_grid_4.the_geom, reccc.the_geom)), vkind FROM e_base_grid_4 
                                             WHERE ST_Intersects(e_base_grid_4.the_geom, reccc.the_geom) AND GeometryType(ST_Intersection(e_base_grid_4.the_geom, reccc.the_geom)) IN('POLYGON','MULTIPOLYGON')  ;

                             END IF;
                         END LOOP;
                     END IF;
                 END LOOP;  
             END IF;
         END LOOP;    

    END;
$$ LANGUAGE plpgsql;










