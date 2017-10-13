package simple.gui;

/** Interface intended to be used as a way to customize how drawing is done on widgets. Widgets have a custom draw that can do some drawing operation before
 * and after the normal widget drawing **/
public interface CustomDraw {
	public void draw(Widget w);
}
