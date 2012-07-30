package org.vn.unit;

import java.util.ArrayList;
import java.util.HashMap;

import org.vn.gl.BaseObject;

public class CharacterManager extends BaseObject {
	public ArrayList<UnitCharacterSwordmen> arrayCharactersMyTeam = new ArrayList<UnitCharacterSwordmen>();
	public ArrayList<UnitCharacterSwordmen> arrayCharactersOtherTeam = new ArrayList<UnitCharacterSwordmen>();
	public HashMap<Integer, UnitCharacterSwordmen> mapEnemyInGame = new HashMap<Integer, UnitCharacterSwordmen>();
	
	@Override
	public void update(float timeDelta, BaseObject parent) {
		for (UnitCharacter character : arrayCharactersMyTeam) {
			character.update(timeDelta, parent);
		}
		for (UnitCharacter character : arrayCharactersOtherTeam) {
			character.update(timeDelta, parent);
		}
	}

	public void setIsMapChangeAndPutAllCharacter(boolean b) {
		mapEnemyInGame.clear();
		for (UnitCharacterSwordmen character : arrayCharactersMyTeam) {
			character.isMapChange = b;
			mapEnemyInGame.put(character.idEnemy, character);
		}
		for (UnitCharacterSwordmen character : arrayCharactersOtherTeam) {
			character.isMapChange = b;
			mapEnemyInGame.put(character.idEnemy, character);
		}
	}

	public void nextTurn() {
		for (UnitCharacterSwordmen character : arrayCharactersMyTeam) {
			character.nextTurn();
		}
		for (UnitCharacterSwordmen character : arrayCharactersOtherTeam) {
			character.nextTurn();
		}
	}
}
