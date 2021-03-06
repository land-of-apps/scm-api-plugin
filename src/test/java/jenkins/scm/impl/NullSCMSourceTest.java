/*
 * The MIT License
 *
 * Copyright (c) 2016 CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package jenkins.scm.impl;

import hudson.scm.NullSCM;
import jenkins.scm.api.SCMHead;
import jenkins.scm.api.SCMHeadObserver;
import jenkins.scm.api.SCMRevision;
import org.junit.ClassRule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

public class NullSCMSourceTest {

    @ClassRule
    public static JenkinsRule r = new JenkinsRule();

    @Test
    public void given_instance_when_fetch_then_noRevisionObserved() throws Exception {
        SCMHeadObserver observer = mock(SCMHeadObserver.class);
        NullSCMSource instance = new NullSCMSource();
        instance.fetch(null, observer, null);
        verify(observer, never()).observe(
                argThat(
                        allOf(
                                instanceOf(SCMHead.class),
                                hasProperty("name", is("the-name"))
                        )
                ),
                argThat(
                        allOf(
                                instanceOf(SCMRevision.class),
                                hasProperty("head", hasProperty("name", is("the-name"))),
                                hasProperty("deterministic", is(false))
                        )
                )
        );
    }

    @Test
    public void given_instance_when_fetchingNonObservedHead_then_nullScmReturned() throws Exception {
        NullSCMSource instance = new NullSCMSource();
        assertThat(instance.build(new SCMHead("foo"), mock(SCMRevision.class)), instanceOf(NullSCM.class));
    }

}
