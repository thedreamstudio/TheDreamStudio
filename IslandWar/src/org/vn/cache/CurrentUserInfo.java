package org.vn.cache;

import org.vn.model.Achievement;
import org.vn.model.PlayerModel;

/**
 * Lưu thông tin đăng nhập của user
 * 
 * @author Quách Toàn Chính
 * 
 */
public class CurrentUserInfo {
	public static String mUsername;
	public static String mPassword;
	public static PlayerModel mPlayerInfo = new PlayerModel();
	public static Achievement mAchievement = null;
}