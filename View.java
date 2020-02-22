package de.cau.infprogoo.game;

/**
 * Classes that want to display the {@link Model} must implement this interface
 * to be notified when the world updates.
 */
public interface View {

	/**
	 * Called whenever the world updates.
	 * 
	 * @param world the {@link Model} object which called this method.
	 */
	void updateStone(Model world);

}
