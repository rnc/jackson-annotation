package com.redhat.cpaas.pipeline.model.component.build


import com.redhat.cpaas.pipeline.model.Repository

public class CEKitBuild extends BrewBuild {
    public static final String TYPE = "cekit"

    /**
     * CEKit version to use.
     */
    String cekitVersion

    /**
     * Optional flags passed to CEKit build command.
     * 
     * TODO: Make it clear at what stage these will be passed since order matters.
     */
    String cekitFlags

    /**
     * Information about the git repository where the CEKit image definition can be found.
     */
    Repository cekitSource
}
