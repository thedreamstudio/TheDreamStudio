package org.vn.gl;

import org.vn.gl.game.CameraSystem;
import org.vn.network.GlobalService;
import org.vn.unit.CharacterManager;
import org.vn.unit.DialogAddEnemy;
import org.vn.unit.InputGameInterface;
import org.vn.unit.LogicMap;
import org.vn.unit.MapTiles;
import org.vn.unit.NumberDrawable;
import org.vn.unit.UnitEffects;
import org.vn.unit.UnitSreen;

/**
 * The object registry manages a collection of global singleton objects.
 * However, it differs from the standard singleton pattern in a few important
 * ways: - The objects managed by the registry have an undefined lifetime. They
 * may become invalid at any time, and they may not be valid at the beginning of
 * the program. - The only object that is always guaranteed to be valid is the
 * ObjectRegistry itself. - There may be more than one ObjectRegistry, and there
 * may be more than one instance of any of the systems managed by ObjectRegistry
 * allocated at once. For example, separate threads may maintain their own
 * separate ObjectRegistry instances.
 */
public class ObjectRegistry extends BaseObject {

	public CameraSystem cameraSystem;
	public ContextParameters contextParameters;

	public InputGameInterface inputGameInterface;
	public OpenGLSystem openGLSystem;
	public TextureLibrary longTermTextureLibrary;
	public TimeSystem timeSystem;
	public RenderSystem renderSystem;
	public SoundSystem soundSystem;
	public MultiTouchFilter2 multiTouchFilter2;
	public NumberDrawable numberDrawableTime;
	public NumberDrawable numberDrawableTakeDame;
	public UnitEffects unitEffects;
	public MapTiles mapTiles;
	public LogicMap logicMap;
	public CharacterManager characterManager;
	public GlobalService mGS = GlobalService.getInstance();
	public UnitSreen unitSreen;
	public DialogAddEnemy dialogAddEnemy;

	public ObjectRegistry() {
		super();
	}

	@Override
	public void reset() {
	}

	public void onDestroy() {
		cameraSystem = null;
		contextParameters = null;
		inputGameInterface = null;
		openGLSystem = null;
		longTermTextureLibrary = null;
		timeSystem = null;
		renderSystem = null;
		characterManager.arrayCharactersMyTeam.clear();
		characterManager.arrayCharactersOtherTeam.clear();
		characterManager.mapEnemyInGame.clear();
		characterManager = null;
	}
}
