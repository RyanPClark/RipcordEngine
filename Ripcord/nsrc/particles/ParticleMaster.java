package particles;



import java.util.ArrayList;

import java.util.Iterator;

import java.util.List;



import org.lwjgl.util.vector.Matrix4f;



import nComponents.Entity;

import nRenderEngine.Loader;



public class ParticleMaster {



	

	private static List<Particle> particles = new ArrayList<Particle>();

	private static ParticleRenderer renderer;

	

	public static void init(Loader loader, Matrix4f matrix){

		renderer = new ParticleRenderer(loader, matrix);

	}

	

	public static void update(){

		Iterator<Particle> iterator = particles.iterator();

		while(iterator.hasNext()){

			Particle p = iterator.next();

			p.update();

			boolean stillAlive = p.update();

			if(!stillAlive){

				iterator.remove();

			}

		}

	}

	

	public static void render(Entity camera){

		renderer.render(particles, camera);

	}

	

	public static void cleanUp(){

		renderer.cleanUp();

	}

	

	public static void addParticle(Particle p){

		particles.add(p);

	}

}