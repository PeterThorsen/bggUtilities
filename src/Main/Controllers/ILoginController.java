package Main.Controllers;

import javax.swing.*;

/**
 * Created by Peter on 06/10/2016.
 */
interface ILoginController {

  FacadeController verifyUser(String username, JTextArea loadingInfoTextArea);
}
