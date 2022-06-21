package com.manydesigns.orientdbmanager;

import com.manydesigns.orientdbmanager.frameworks.AcceptedFramework;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.ParseException;
import java.util.MissingFormatArgumentException;

/**
 * Author: Emanuele Collura
 * Date: 20/06/22
 * Time: 17:47
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MangerArgs {

    private AcceptedFramework acceptedFramework;
    private String openApi;
    private String commInjResult;

    public static MangerArgs parse(String[] args) throws MissingFormatArgumentException, ParseException {
        var managerArgs = new MangerArgs();
        for (var i = 0; i < args.length; i += 2) {
            var arg = args[i];

            if (arg.equalsIgnoreCase("-f")) {
                managerArgs.setAcceptedFramework(AcceptedFramework.parse(args[i + 1]));
            } else if (arg.equalsIgnoreCase("-o")) {
                managerArgs.setOpenApi(args[i + 1]);
            } else if (arg.equalsIgnoreCase("-r")) {
                managerArgs.setCommInjResult(args[i + 1]);
            }
        }

        if (managerArgs.getCommInjResult() == null || managerArgs.getAcceptedFramework() == null)
            throw new MissingFormatArgumentException("missing arguments");

        return managerArgs;
    }

}
