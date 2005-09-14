
/*
 * Carrot2 Project
 * Copyright (C) 2002-2004, Dawid Weiss
 * Portions (C) Contributors listed in carrot2.CONTRIBUTORS file.
 * All rights reserved.
 *
 * Refer to the full license file "carrot2.LICENSE"
 * in the root folder of the CVS checkout or at:
 * http://www.cs.put.poznan.pl/dweiss/carrot2.LICENSE
 *
 * Sponsored by: CCG, Inc.
 */

package com.dawidweiss.carrot.local.controller;

import com.dawidweiss.carrot.core.local.*;

import java.util.Collections;
import java.util.Set;


/**
 * A stub implementation of an input component for tests.
 */
public class StubInputComponent extends StubComponent
    implements LocalInputComponent {
    /* */
    private String query;

    /* */
    private LocalControllerContext context;

    /* */
    public StubInputComponent(String id, Set componentCapabilities,
        Set predecessorCapabilities, Set successorCapabilities) {
        super(id, componentCapabilities, predecessorCapabilities,
            successorCapabilities, "i", "Input component stub.");
    }

    /* */
    public StubInputComponent(Set componentCapabilities,
        Set predecessorCapabilities, Set successorCapabilities) {
        this("i", componentCapabilities, predecessorCapabilities,
            successorCapabilities);
    }

    /* */
    public StubInputComponent() {
        this("i", Collections.EMPTY_SET, Collections.EMPTY_SET,
            Collections.EMPTY_SET);
    }

    /* */
    public void setQuery(String query) {
        this.query = query;
    }
}
