package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;

import dev.lyze.projectTrianglePlatforming.TiledObjectLayerToBox2d;
import dev.lyze.projectTrianglePlatforming.TiledObjectLayerToBox2dOptions;

public class learnbox extends ApplicationAdapter {
    SpriteBatch batch ; World world;    TiledMap map ;
    OrthogonalTiledMapRenderer renderer ;
    float cameraWidth;float cameraHeight;
    OrthographicCamera camera; Body body;float ppm = 1000.0f;
    Box2DDebugRenderer dr;
    public void create(){
        map = new TmxMapLoader().load("block.tmx");

        renderer = new OrthogonalTiledMapRenderer(map,1/ppm);
        world = new World(new Vector2(0,-9.8f),true);dr = new Box2DDebugRenderer();
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        float tileWidths = 16; // Tile size in pixels
        float tileHeight = 16;
        float mapWidth = 60; // Map size in tiles
        int mapHeight = 100;
        cameraWidth = tileWidths * mapWidth;
        cameraHeight = tileHeight * mapHeight;
        var tileWidth = map.getProperties().get("tilewidth", Integer.class);

        System.out.println(tileWidth);
        var builder = new TiledObjectLayerToBox2d(TiledObjectLayerToBox2dOptions.builder()
                .scale(1f / cameraWidth)
                .throwOnInvalidObject(false)
                .build());
        builder.parseAllLayers(map, world);
        camera.setToOrtho(false,cameraWidth/ppm,cameraHeight/ppm);

        body = createobject(world,80,500, BodyDef.BodyType.DynamicBody,true,0,0);



    }
    public void render(){
      //  ScreenUtils.clear(0.9f, 0.1f, 0.5f, 1);
        ScreenUtils.clear(0,0,0,1);
        renderer.setView(camera);
        renderer.render();
        batch.setProjectionMatrix(camera.combined);
        world.step(Gdx.graphics.getDeltaTime(),6,2);
        dr.render(world,camera.combined);
       // System.out.println(body.getPosition().y);
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            body.applyForceToCenter(new Vector2(2/ppm,0),true);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            body.applyForceToCenter(new Vector2(-2/ppm,0),true);
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            body.applyForceToCenter(new Vector2(0,80/ppm),true);
        }
    }
    public Body createobject(World world, int x, int y, BodyDef.BodyType type,boolean circle,int a,int b){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = type;
        bodyDef.position.set(new Vector2(x/ppm, y/ppm));
        Body body = world.createBody(bodyDef);
        if(circle){
        CircleShape shape = new CircleShape();
       shape.setRadius(40/ppm);
      body.createFixture(shape,0.5f);
        }
        else {
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(a/ppm,b/ppm);
            body.createFixture(shape,0.5f);

        }

        return body;

    }
}
