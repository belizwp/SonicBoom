package com.oop.sonicboom;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class WorldContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();

		// collision define
		int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

		switch (cDef) {
		case SonicBoom.PLAYER_BIT | SonicBoom.GROUND_BIT:
		case SonicBoom.PLAYER_BIT | SonicBoom.PLATFORM_BIT:
			if (fixA.getFilterData().categoryBits == SonicBoom.PLAYER_BIT) {
				if (((Object) fixB.getUserData()) instanceof GameObject) {
					((GameObject) fixB.getUserData()).hit();
				}

			} else {
				if (((Object) fixA.getUserData()) instanceof GameObject) {
					((GameObject) fixA.getUserData()).hit();
				}
			}
			break;
		case SonicBoom.PLAYER_BIT | SonicBoom.RING_BIT:
			if (fixA.getFilterData().categoryBits == SonicBoom.PLAYER_BIT) {
				((Ring) fixB.getUserData()).hit();
			} else {
				((Ring) fixA.getUserData()).hit();
			}
			break;
		case SonicBoom.PLAYER_BIT | SonicBoom.LOOP_SWITCH_BIT:
			if (fixA.getFilterData().categoryBits == SonicBoom.PLAYER_BIT) {
				((Player) fixA.getUserData()).switchLoop();
			} else {
				((Player) fixB.getUserData()).switchLoop();
			}
			break;
		case SonicBoom.PLAYER_BIT | SonicBoom.LOOP_R_SENSOR_BIT:
			if (fixA.getFilterData().categoryBits == SonicBoom.PLAYER_BIT) {
				((Player) fixA.getUserData()).loop = true;
			} else {
				((Player) fixB.getUserData()).loop = true;
			}
			break;
		case SonicBoom.PLAYER_BIT | SonicBoom.LOOP_L_SENSOR_BIT:
			if (fixA.getFilterData().categoryBits == SonicBoom.PLAYER_BIT) {
				((Player) fixA.getUserData()).loop = false;
			} else {
				((Player) fixB.getUserData()).loop = false;
			}
			break;
		case SonicBoom.PLAYER_BIT | SonicBoom.OBJECT_BIT:
			if (fixA.getFilterData().categoryBits == SonicBoom.PLAYER_BIT) {
				((GameObject) fixB.getUserData()).hit();
			} else {
				((GameObject) fixA.getUserData()).hit();
			}
			break;
		case SonicBoom.PLAYER_BIT | SonicBoom.ENEMY_BIT:
			if (fixA.getFilterData().categoryBits == SonicBoom.PLAYER_BIT) {
				((Enemy) fixB.getUserData()).hit();
			} else {
				((Enemy) fixA.getUserData()).hit();
			}
			break;
		}

	}

	@Override
	public void endContact(Contact contact) {
		// reset the default state of the contact in case it comes back for more
		contact.setEnabled(true);

		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();

		// collision define
		int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

		switch (cDef) {
		case SonicBoom.PLAYER_BIT | SonicBoom.GROUND_BIT:
		case SonicBoom.PLAYER_BIT | SonicBoom.PLATFORM_BIT:
			if (fixA.getFilterData().categoryBits == SonicBoom.PLAYER_BIT) {
				((Player) fixA.getUserData()).onGround = false;

			} else {
				((Player) fixB.getUserData()).onGround = false;
			}
			break;
		case SonicBoom.PLAYER_BIT | SonicBoom.LOOP_R_BIT:
			if (fixA.getFilterData().categoryBits == SonicBoom.PLAYER_BIT) {
				((Player) fixA.getUserData()).onLoop = false;
			} else {
				((Player) fixB.getUserData()).onLoop = false;
			}
			break;
		case SonicBoom.PLAYER_BIT | SonicBoom.LOOP_L_BIT:
			if (fixA.getFilterData().categoryBits == SonicBoom.PLAYER_BIT) {
				((Player) fixA.getUserData()).onLoop = false;
			} else {
				((Player) fixB.getUserData()).onLoop = false;
			}
			break;
		}

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();

		// collision define
		int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

		switch (cDef) {
		case SonicBoom.PLAYER_BIT | SonicBoom.GROUND_BIT:
			if (fixA.getFilterData().categoryBits == SonicBoom.PLAYER_BIT) {
				((Player) fixA.getUserData()).onGround = true;
				((Player) fixA.getUserData()).contactPoint = contact.getWorldManifold().getPoints()[0];

			} else {
				((Player) fixB.getUserData()).onGround = true;
				((Player) fixB.getUserData()).contactPoint = contact.getWorldManifold().getPoints()[0];
			}
			break;
		case SonicBoom.PLAYER_BIT | SonicBoom.PLATFORM_BIT:
			Body playerBody = null;

			if (fixA.getFilterData().categoryBits == SonicBoom.PLAYER_BIT) {
				playerBody = fixA.getBody();

				Player player = ((Player) fixA.getUserData());

				if (!player.spinJump || !player.spinning) {
					player.onGround = true;
				}

				((Player) fixA.getUserData()).contactPoint = contact.getWorldManifold().getPoints()[0];

			} else {
				playerBody = fixB.getBody();

				Player player = ((Player) fixB.getUserData());

				if (!player.spinJump || !player.spinning) {
					player.onGround = true;
				}

				((Player) fixB.getUserData()).onGround = true;
				((Player) fixB.getUserData()).contactPoint = contact.getWorldManifold().getPoints()[0];

			}

			Vector2 p1 = contact.getWorldManifold().getPoints()[0];
			Vector2 p2 = playerBody.getWorldCenter();

			float contactAngle = (float) (Math.atan2(p2.y - p1.y, p2.x - p1.x) * 180 / Math.PI) - 90;

			if (contactAngle < -180) {
				contactAngle += 360;
			}

			if (contactAngle >= 120 || contactAngle <= -120) {
				contact.setEnabled(false);
			} else if (p1.dst(p2) > 0.12f) {
				return;
			} else {
				contact.setEnabled(false);
			}

			break;
		case SonicBoom.PLAYER_BIT | SonicBoom.LOOP_R_BIT:
			if (fixA.getFilterData().categoryBits == SonicBoom.PLAYER_BIT) {
				if (((Player) fixA.getUserData()).loop == false) {
					contact.setEnabled(false);
				} else {
					((Player) fixA.getUserData()).contactPoint = contact.getWorldManifold().getPoints()[0];
					((Player) fixA.getUserData()).onLoop = true;
				}

			} else {
				if (((Player) fixB.getUserData()).loop == false) {
					contact.setEnabled(false);
				} else {
					((Player) fixB.getUserData()).contactPoint = contact.getWorldManifold().getPoints()[0];
					((Player) fixB.getUserData()).onLoop = true;
				}
			}
			break;
		case SonicBoom.PLAYER_BIT | SonicBoom.LOOP_L_BIT:
			if (fixA.getFilterData().categoryBits == SonicBoom.PLAYER_BIT) {
				if (((Player) fixA.getUserData()).loop == true) {
					contact.setEnabled(false);
				} else {
					((Player) fixA.getUserData()).contactPoint = contact.getWorldManifold().getPoints()[0];
					((Player) fixA.getUserData()).onLoop = true;
				}

			} else {
				if (((Player) fixB.getUserData()).loop == true) {
					contact.setEnabled(false);
				} else {
					((Player) fixB.getUserData()).contactPoint = contact.getWorldManifold().getPoints()[0];
					((Player) fixB.getUserData()).onLoop = true;
				}
			}
			break;
		case SonicBoom.PLAYER_BIT | SonicBoom.RING_BIT:
			contact.setEnabled(false);
		}

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}
