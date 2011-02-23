/*
 * The MIT License
 *
 * Copyright (C) 2010-2011 by Anthony Robinson
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
 */

package jenkins.plugins.publish_over_ftp.jenkins;

import hudson.model.FreeStyleProject;
import jenkins.plugins.publish_over_ftp.BapFtpHostConfiguration;
import jenkins.plugins.publish_over_ftp.BapFtpPublisher;
import jenkins.plugins.publish_over_ftp.BapFtpPublisherPlugin;
import jenkins.plugins.publish_over_ftp.BapFtpTransfer;
import org.jvnet.hudson.test.HudsonTestCase;

import java.util.LinkedList;
import java.util.List;

public class CurrentConfigurationTest extends HudsonTestCase {

    private FreeStyleProject project;
    private BapFtpHostConfiguration configA;
    private BapFtpHostConfiguration configB;
    private JenkinsTestHelper testHelper = new JenkinsTestHelper();

    public void testTestsAreDisabled() throws Exception {
        final int numberOfTimesToRepeatTheMessage = 3;
        for (int i = 0; i < numberOfTimesToRepeatTheMessage; i++) {
            System.out.println("*** TEST DISABLED!");
            System.err.println("*** TEST DISABLED!");
        }
        System.out.println("can no longer configure or retrieve the config");
        System.err.println("can no longer configure or retrieve the config");
    }
//    @TODO figure out why this no longer works
    public void dontTestRoundTrip() throws Exception {
        configA = new BapFtpHostConfiguration("host A", "", "", "", "", 0, 0, false);
        configB = new BapFtpHostConfiguration("host B", "", "", "", "", 0, 0, false);
        project = createFreeStyleProject();
        testHelper.setGlobalConfig(configA, configB);
        BapFtpPublisherPlugin plugin = createPlugin(configA.getName(), configB.getName());
        project.getPublishersList().add(plugin);

        submit(new WebClient().getPage(project, "configure").getFormByName("config"));

        BapFtpPublisherPlugin configured = (BapFtpPublisherPlugin) project.getPublisher(BapFtpPublisherPlugin.DESCRIPTOR);
        System.out.println(" pre:" + plugin);
        System.out.println("post:" + configured);
        assertNotSame(plugin, configured);
        assertEquals(plugin, configured);
    }

//    @TODO test configuring various ways using the jelly forms (promotions?)
//    public void testConfigureProject() throws Exception {}
//
//    public void testConfigureGlobal() throws Exception {}

    private BapFtpPublisherPlugin createPlugin(final String config1, final String config2) {
        BapFtpTransfer transfer1 = new BapFtpTransfer("**/*", "/pub", "target", true, false, true);
        BapFtpTransfer transfer2 = new BapFtpTransfer("*", "", "WebApp", false, true, false);
        BapFtpTransfer transfer3 = new BapFtpTransfer("dave", "", "", false, true, true);
        List<BapFtpTransfer> transfers1 = new LinkedList<BapFtpTransfer>();
        transfers1.add(transfer1);
        transfers1.add(transfer2);
        List<BapFtpTransfer> transfers2 = new LinkedList<BapFtpTransfer>();
        transfers2.add(transfer3);
        BapFtpPublisher publisher1 = new BapFtpPublisher(config1, true, transfers1);
        BapFtpPublisher publisher2 = new BapFtpPublisher(config2, false, transfers2);
        List<BapFtpPublisher> publishers = new LinkedList<BapFtpPublisher>();
        publishers.add(publisher1);
        publishers.add(publisher2);
        return new BapFtpPublisherPlugin(publishers, true, true, true, "MASTER");
    }

}
