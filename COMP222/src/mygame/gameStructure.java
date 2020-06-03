package mygame;

/**
 *
 * @author Wenjia Wang 201448494
 */
import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.PointLightShadowRenderer;
import com.jme3.ui.Picture;
import com.jme3.util.SkyFactory;
import com.jme3.texture.Texture;
import java.util.Random;

/**
 * The structure of the game
 *
 *
 */
public class gameStructure extends SimpleApplication {

    //collision sound ball collide with table and paddle
    private AudioNode collisionSound1;
    //collision sound ball collide with balls
    private AudioNode collisionSound2;
    //backgroud music
    private AudioNode backgroundMusic;
    //winning sound
    private AudioNode winningSound;
    //start interface, winning interface and restart interface
    private Picture p = new Picture("Picture");
    //speed of the ball's rotation
    private int rotationSpeed;
    //text showing the score and level
    private BitmapText text;
    //the score in each level
    private int score;
    //the level of the game
    private int level;
    //the football and target balls
    private Node ball;
    private Node ball_2, ball_3, ball_4, ball_5, ball_6;
    //table and paddle node
    private Node table;
    private Node paddle;
    //speed of ball
    Vector3f velocity;
    //vector
    final Vector3f upVector = new Vector3f((float) -4.7, 0, (float) -5.9);
    final Vector3f downVector = new Vector3f((float) -4.7, 0, (float) 5.9);
    final Vector3f leftVector = new Vector3f((float) -4.7, 0, (float) -5.9);
    final Vector3f rightVector = new Vector3f((float) 4.7, 0, (float) -5.9);
    //the collision of the ball and the up, down, lefg and right side of the table
    Ray up = new Ray(upVector, Vector3f.UNIT_X);
    Ray down = new Ray(downVector, Vector3f.UNIT_X);
    //the left side of the table
    Ray left = new Ray(leftVector, Vector3f.UNIT_Z);
    Ray right = new Ray(rightVector, Vector3f.UNIT_Z);

    @Override
    public void simpleInitApp() {
        //set the camera as stabale 
        this.flyCam.setEnabled(false);
        this.setDisplayFps(false);
        this.setDisplayStatView(false);
        //background music 
        backgroundMusic = new AudioNode(assetManager, "Sounds/S31-Night Prowler.ogg", false);
        backgroundMusic.setPositional(false);
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.1f);
        backgroundMusic.play();
        //the music of the ball colliding with the table and paddle
        collisionSound1 = new AudioNode(assetManager, "Sounds/hit.wav", false);
        collisionSound1.setPositional(false);
        collisionSound1.setLooping(false);
        collisionSound1.setVolume(0.5f);
        //the music of the collision between ball and ball 
        collisionSound2 = new AudioNode(assetManager, "Sounds/Mario.wav", false);
        collisionSound2.setPositional(false);
        collisionSound2.setLooping(false);
        collisionSound2.setVolume(0.5f);
        //the music of winning
        winningSound = new AudioNode(assetManager, "Sounds/win.ogg", false);
        winningSound.setPositional(false);
        winningSound.setLooping(false);
        winningSound.setVolume(0.5f);

        //text showing the level and score
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        text = new BitmapText(guiFont);
        text.setSize(guiFont.getCharSet().getRenderedSize());
        //set the position
        text.move(settings.getWidth() / 30, text.getLineHeight() + 500, 0);
        text.setSize(38f);
        guiNode.attachChild(text);

        // load the table
        table = (Node) assetManager.loadModel("Models/table.j3o");
        rootNode.attachChild(table);

        //set the balls' position
        ball = (Node) assetManager.loadModel("Models/1.j3o");
        ball.scale(0.4f);
        rootNode.attachChild(ball);
        //load the other all node
        ball_2 = (Node) assetManager.loadModel("Models/2.j3o");
        ball_3 = (Node) assetManager.loadModel("Models/3.j3o");
        ball_4 = (Node) assetManager.loadModel("Models/4.j3o");
        ball_2.scale(0.5f);
        ball_3.scale(0.35f);
        ball_4.scale(0.7f);
        rootNode.attachChild(ball_2);
        rootNode.attachChild(ball_3);
        rootNode.attachChild(ball_4);
        ball_5 = (Node) assetManager.loadModel("Models/5.j3o");
        ball_5.scale(0.35f);
        //the position to store the balls after collision
        ball_5.setLocalTranslation(10, 10, 10);
        rootNode.attachChild(ball_5);
        ball_6 = (Node) assetManager.loadModel("Models/6.j3o");
        ball_6.scale(0.4f);
        ball_6.setLocalTranslation(10, 10, 10);
        rootNode.attachChild(ball_6);
        //load the paddle and set the paddle's position
        paddle = (Node) assetManager.loadModel("Models/board.j3o");
        paddle.scale(1, 1, 0.7f);
        rootNode.attachChild(paddle);
        /*
         * input event handle 
         * 1. move paddle to the left: KEY_RIGHT
         * 2. move paddle to the right: KEY_LEFT
         * 3. start: key W
         * 4. pause: key A
         * 5. restart & speed up : key D
         */
        inputManager.addMapping("Move right",
                new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("Move left",
                new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("start",
                new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("pause",
                new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Restart",
                new KeyTrigger(KeyInput.KEY_D));
        inputManager.addListener(analogListener, "Move right");
        inputManager.addListener(analogListener, "Move left");
        inputManager.addListener(analogListener, "Restart");
        inputManager.addListener(analogListener, "pause");
        inputManager.addListener(analogListener, "start");
        // Set up the light
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1, -1, -1));
        rootNode.addLight(sun);
        PointLight myLight = new PointLight();
        myLight.setColor(ColorRGBA.White);
        myLight.setColor(ColorRGBA.White.mult(2f));
        myLight.setPosition(new Vector3f(0, 2, 2));
        myLight.setRadius(20);
        rootNode.addLight(myLight);
        // Casting shadows
        ball.setShadowMode(RenderQueue.ShadowMode.Cast);
        table.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        // set up the renderers
        PointLightShadowRenderer plsr = new PointLightShadowRenderer(assetManager, 512);
        plsr.setLight(myLight);
        plsr.setFlushQueues(false);
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, 512, 2);
        dlsr.setLight(sun);
        // add to the viewport
        viewPort.addProcessor(plsr);
        viewPort.addProcessor(dlsr);
        // set up the camera 
        cam.setLocation(new Vector3f(0, 17, 6));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        cam.clearViewportChanged();
        // set up the background
        Texture west = assetManager.loadTexture("Textures/sky.jpg");
        Texture east = assetManager.loadTexture("Textures/sky.jpg");
        Texture north = assetManager.loadTexture("Textures/sky.jpg");
        Texture south = assetManager.loadTexture("Textures/sky.jpg");
        Texture up = assetManager.loadTexture("Textures/sky.jpg");
        Texture down = assetManager.loadTexture("Textures/sky.jpg");
        Spatial sky = SkyFactory.createSky(assetManager, west, east, north, south, up, down);
        rootNode.attachChild(sky);

        //set up the user interface
        p.move(0, 0, 1);
        p.setPosition(0, 0);
        p.setWidth(settings.getWidth());
        p.setHeight(settings.getHeight());
        p.setImage(assetManager, "Interface/start_interface.jpg", false);
        // attach geometry to orthoNode
        guiNode.attachChild(p);
        velocity = new Vector3f(0, 0, 0);
        ball.setLocalTranslation(0, 0, 3);
    }

    // handle the keyboard input 
    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float value, float tpf) {
            if (name.equals("Move right")) {
                paddle.move(7 * tpf, 0, 0);
            } else if (name.equals("Move left")) {
                paddle.move(-7 * tpf, 0, 0);
            } else if (name.equals("start")) {
                //put all the balls into a invisiable position
                ball_2.setLocalTranslation(10, 10, 10);
                ball_3.setLocalTranslation(10, 10, 10);
                ball_4.setLocalTranslation(10, 10, 10);
                ball_5.setLocalTranslation(10, 10, 10);
                ball_6.setLocalTranslation(10, 10, 10);
                //set the size of picture into 0*0
                p.setWidth(0);
                p.setHeight(0);
                //restart the level 1
                Level_1();
            } else if (name.equals("pause")) {
                //pause the ball
                velocity.setX(0);
                velocity.setY(0);
                velocity.setZ(0);
            } else if (name.equals("Restart")) {
                //restart and speed up the ball
                velocity.setZ(velocity.getZ() + 0.2f);
            }
        }
    };

    /**
     * stop the game when the football is out of the boundary
     */
    protected void Stop() {
        rotationSpeed = 0;
        velocity = new Vector3f(0, 0, 0);
        ball.setLocalTranslation(0, 0, (float) (6 - 0.3 * 2.5));
        paddle.setLocalTranslation(0, 0, 10);
        // show the restart interface
        p.move(0, 0, 1);
        p.setPosition(0, 0);
        p.setWidth(settings.getWidth());
        p.setHeight(settings.getHeight());
        p.setImage(assetManager, "Interface/gameover_interface.jpg", false);
    }

    /**
     * This is the fisrt level of the game
     *
     */
    protected void Level_1() {
        level = 1;
        score = 0;
        rotationSpeed = 2;
        float num = new Random().nextFloat() * 2 - 1;
        velocity = new Vector3f(num, 0, -1);
        velocity = velocity.mult(5f);
        ball.setLocalTranslation(0, 0, (float) (6 - 0.35 * 2));
        //set the position of ball2, ball3 and ball4 in level_1
        ball_2.setLocalTranslation(1f, 0, 1f);
        ball_3.setLocalTranslation(2f, 0, -2f);
        ball_4.setLocalTranslation(-2f, 0, -2f);
        paddle.setLocalTranslation(0, 0, 6);
    }

    /**
     * This is the second level of the game.
     * The speed of the football is increased and the target ball's number is increased
     *
     */
    protected void Level_2() {
        level = 2;
        score = 0;
        rotationSpeed = 3;
        float num = new Random().nextFloat() * 2 - 1;
        velocity = new Vector3f(num, 0, -1);
        velocity = velocity.mult(7f);
        ball.setLocalTranslation(0, 0, (float) (6 - 0.3 * 3));
        ball_2.setLocalTranslation(-2f, 0, -2f);
        ball_3.setLocalTranslation(2f, 0, -2f);
        ball_4.setLocalTranslation(-2f, 0, 2f);
        ball_5.setLocalTranslation(2f, 0, 2f);
        paddle.setLocalTranslation(0, 0, 6);
    }

    /**
     * This is the third level of the game. The speed of the football is
     * increased and the target ball's number is increased.
     *
     */
    protected void Level_3() {
        level = 3;
        score = 0;
        rotationSpeed = 4;
        float num = new Random().nextFloat() * 2 - 1;
        velocity = new Vector3f(num, 0, -1);
        velocity = velocity.mult(12f);
        //set up all the position of the ball
        ball.setLocalTranslation(0, 0, (float) (6 - 0.3 * 3));
        ball_2.setLocalTranslation(0, 0, 3f);
        ball_3.setLocalTranslation(-2f, 0, 0);
        ball_4.setLocalTranslation(2f, 0, 0);
        ball_5.setLocalTranslation(-1f, 0, -3f);
        ball_6.setLocalTranslation(1f, 0, -3f);
        paddle.setLocalTranslation(0, 0, 6);
    }

    // show the interface when the user win the game
    protected void Win() {
        p.move(0, 0, 1);
        p.setPosition(0, 0);
        p.setWidth(settings.getWidth());
        p.setHeight(settings.getHeight());
        p.setImage(assetManager, "Interface/winning_interface.jpg", false);
        winningSound.play();
        velocity = velocity.mult(0f);
    }

    @Override
    public void simpleUpdate(float tpf) {
        text.setText("Level " + level + "\n" + "Score: " + (score));
        ball.rotate(0, rotationSpeed * FastMath.PI * tpf, 0);
        ball.move(velocity.mult(tpf));
        //when the football collides with the table
        CollisionResults resultsPaddle = new CollisionResults();
        BoundingVolume bvBord = paddle.getWorldBound();
        table.collideWith(bvBord, resultsPaddle);
        if (resultsPaddle.size() > 0) {
            //Restrict the move range of the paddle 
            if (paddle.getLocalTranslation().x < 0) {
                paddle.setLocalTranslation((float) -3.65, 0, 6);
            } else {
                paddle.setLocalTranslation((float) 3.65, 0, 6);
            }
        }
        //the collision between football and table
        CollisionResults resultUp = new CollisionResults();
        CollisionResults resultDown = new CollisionResults();
        CollisionResults resultLeft = new CollisionResults();
        CollisionResults resultRight = new CollisionResults();
        ball.collideWith(up, resultUp);
        ball.collideWith(down, resultDown);
        ball.collideWith(left, resultLeft);
        ball.collideWith(right, resultRight);
        if (resultUp.size() > 0) {
            velocity.setZ(FastMath.abs(velocity.getZ()));
            velocity = velocity.mult(1.005f);
            collisionSound1.playInstance();

        } else if (resultDown.size() > 0) {
            Stop();

        } else if (resultLeft.size() > 0) {
            velocity.setX(FastMath.abs(velocity.getX()));
            velocity = velocity.mult(1.005f);
            collisionSound1.playInstance();

        } else if (resultRight.size() > 0) {
            velocity.setX(-FastMath.abs(velocity.getX()));
            velocity = velocity.mult(1.005f);
            collisionSound1.playInstance();
        }

        // if the ball collide with the paddle
        CollisionResults resultBallPaddle = new CollisionResults();
        BoundingVolume bvBall = ball.getWorldBound();
        paddle.collideWith(bvBall, resultBallPaddle);

        if (resultBallPaddle.size() > 0) {
            collisionSound1.playInstance();
            velocity.setZ(-FastMath.abs(velocity.getZ()));
            //friction on x axis
            if (paddle.getLocalTranslation().getX() < 0) {
                velocity.setX(velocity.getX() + 0.5f);

            } else if (paddle.getLocalTranslation().getX() > 0) {
                velocity.setX(velocity.getX() - 0.5f);

            }

            // when the football collides with the table, change the colour of the paddle
            Material boxMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            boxMat.setBoolean("UseMaterialColors", true);
            boxMat.setColor("Diffuse", ColorRGBA.randomColor());
            paddle.setMaterial(boxMat);
        }
        //when football collides with ball2
        CollisionResults resultBallBall_2 = new CollisionResults();
        BoundingVolume bvBall_2 = ball_2.getWorldBound();
        ball.collideWith(bvBall_2, resultBallBall_2);

        if (resultBallBall_2.size() > 0) {
            collisionSound2.playInstance();
            score += 1;
            Vector3f norm = new Vector3f((ball_2.getLocalTranslation().subtract(ball.getLocalTranslation())).normalize());
            float projVal = velocity.dot(norm);
            Vector3f projection = norm.mult(projVal);
            Vector3f parall = velocity.subtract(projection);
            velocity = parall.subtract(projection);
            //move balll2 to a position so that it is invisiable from the user
            ball_2.move(10, 10, 10);
        }
        //when football collides with ball3
        CollisionResults resultBallBall_3 = new CollisionResults();
        BoundingVolume bvBall_3 = ball_3.getWorldBound();
        ball.collideWith(bvBall_3, resultBallBall_3);

        if (resultBallBall_3.size() > 0) {

            collisionSound2.playInstance();
            score += 1;
            Vector3f norm = new Vector3f(ball_3.getLocalTranslation().subtract(ball.getLocalTranslation()).normalize());
            float projVal = velocity.dot(norm);
            Vector3f projection = norm.mult(projVal);
            Vector3f parall = velocity.subtract(projection);
            velocity = parall.subtract(projection);
            ball_3.move(10, 10, 10);
        }
        //when football collides with ball4
        CollisionResults resultBallBall_4 = new CollisionResults();
        BoundingVolume bvBall_4 = ball_4.getWorldBound();
        ball.collideWith(bvBall_4, resultBallBall_4);

        if (resultBallBall_4.size() > 0) {

            collisionSound2.playInstance();
            score += 1;
            Vector3f norm = new Vector3f(ball_4.getLocalTranslation().subtract(ball.getLocalTranslation()).normalize());
            float projVal = velocity.dot(norm);
            Vector3f projection = norm.mult(projVal);
            Vector3f parall = velocity.subtract(projection);
            velocity = parall.subtract(projection);
            ball_4.move(10, 10, 10);
        }

        //when football collides with ball5
        CollisionResults resultBallBall_5 = new CollisionResults();
        BoundingVolume bvBall_5 = ball_5.getWorldBound();
        ball.collideWith(bvBall_5, resultBallBall_5);

        if (resultBallBall_5.size() > 0) {

            collisionSound2.playInstance();
            score += 1;
            //normal vector from ball to ball2
            Vector3f norm = new Vector3f(ball_5.getLocalTranslation().subtract(ball.getLocalTranslation()).normalize());
            //length of projection on norm
            float projVal = velocity.dot(norm);
            //vector projrction
            Vector3f projection = norm.mult(projVal);
            //parall vector
            Vector3f parall = velocity.subtract(projection);
            velocity = parall.subtract(projection);
            ball_5.move(10, 10, 10);// move all ball here after collision 

        }

        //when football collides with ball6
        CollisionResults resultBallBall_6 = new CollisionResults();
        BoundingVolume bvBall_6 = ball_6.getWorldBound();
        ball.collideWith(bvBall_6, resultBallBall_6);

        if (resultBallBall_6.size() > 0) {

            collisionSound2.playInstance();
            score += 1;
            Vector3f norm = new Vector3f(ball_6.getLocalTranslation().subtract(ball.getLocalTranslation()).normalize());
            float projVal = velocity.dot(norm);
            Vector3f projection = norm.mult(projVal);
            Vector3f parall = velocity.subtract(projection);
            velocity = parall.subtract(projection);
            ball_6.move(10, 10, 10);
        }

        if (score == 3 && level == 1) {
            Level_2();
        }
        if (score == 4 && level == 2) {
            Level_3();
        }
        if (score == 5 && level == 3) {
            Win();
        }
    }
}
