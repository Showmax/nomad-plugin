package org.jenkinsci.plugins.nomad;

import hudson.Extension;
import hudson.Util;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Label;
import hudson.model.Node;
import hudson.model.labels.LabelAtom;
import jenkins.model.Jenkins;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.*;
import java.util.logging.Logger;

public class NomadSlaveTemplate implements Describable<NomadSlaveTemplate> {

    private static final String SLAVE_PREFIX = "jenkins";
    private static final Logger LOGGER = Logger.getLogger(NomadSlaveTemplate.class.getName());

    private final int idleTerminationInMinutes;
    private final Boolean reusable;
    private final int numExecutors;

    private final String prefix;
    private final int cpu;
    private final int memory;
    private final int disk;
    private final int priority;
    private final String labels;
    private final List<? extends NomadConstraintTemplate> constraints;
    private final String region;
    private final String remoteFs;
    private final Boolean useRawExec;
    private final String image;
    private final Boolean privileged;
    private final String network;
    private final String username;
    private final String password;
    private final String prefixCmd;
    private final Boolean forcePull;
    private final String sharedMem;
    private final String hostVolumes;
    private final String hostDevices;
    private final String switchUser;
    private final Node.Mode mode;
    private final List<? extends NomadPortTemplate> ports;
    private final String extraHosts;
    private final String capAdd;
    private final String capDrop;


    private String driver;
    private String datacenters;
    private Set<LabelAtom> labelSet;

    @DataBoundConstructor
    public NomadSlaveTemplate(
            String prefix,
            String cpu,
            String memory,
            String disk,
            String labels,
            List<? extends NomadConstraintTemplate> constraints,
            String remoteFs,
            Boolean useRawExec,
            String idleTerminationInMinutes,
            Boolean reusable,
            String numExecutors,
            Node.Mode mode,
            String region,
            String priority,
            String image,
            String datacenters,
            String username,
            String password,
            Boolean privileged,
            String network,
            String prefixCmd,
            String sharedMemory,
            Boolean forcePull,
            String hostVolumes,
            String hostDevices,
            String switchUser,
            List<? extends NomadPortTemplate> ports,
            String extraHosts,
            String capAdd,
            String capDrop
    ) {
        if (StringUtils.isNotEmpty(prefix))
            this.prefix = prefix;
        else
            this.prefix = SLAVE_PREFIX;

        this.cpu = Integer.parseInt(cpu);
        this.memory = Integer.parseInt(memory);
        this.disk = Integer.parseInt(disk);
        this.priority = Integer.parseInt(priority);
        this.idleTerminationInMinutes = Integer.parseInt(idleTerminationInMinutes);
        this.reusable = reusable;
        this.numExecutors = Integer.parseInt(numExecutors);
        this.mode = mode;
        this.remoteFs = remoteFs;
        if (useRawExec != null) {
            this.useRawExec = useRawExec;
        } else {
            this.useRawExec = false;
        }
        this.labels = Util.fixNull(labels);
        this.labelSet = Label.parse(this.labels);

        if (constraints == null) {
            this.constraints = Collections.emptyList();
        } else {
            this.constraints = constraints;
        }

        this.region = region;
        this.image = image;
        this.datacenters = datacenters;
        this.username = username;
        this.password = password;
        this.privileged = privileged;
        this.network = network;
        this.prefixCmd = prefixCmd;
        if (switchUser == null) {
            this.switchUser = "";
        } else {
            this.switchUser = switchUser;
        }
        this.forcePull = forcePull;
        this.sharedMem = sharedMemory;
        this.hostVolumes = hostVolumes;
        this.hostDevices = hostDevices;
        if (ports == null) {
            this.ports = Collections.emptyList();
        } else {
            this.ports = ports;
        }
        this.extraHosts = Util.fixNull(extraHosts);
        this.capAdd = Util.fixNull(capAdd);
        this.capDrop = Util.fixNull(capDrop);
        readResolve();
    }

    protected Object readResolve() {
        this.driver = !this.image.equals("") ? "docker" : "java";
        if (this.useRawExec != null && this.useRawExec) this.driver = "raw_exec";
        return this;
    }


    @Extension
    public static final class DescriptorImpl extends Descriptor<NomadSlaveTemplate> {

        public DescriptorImpl() {
            load();
        }

        @Override
        public String getDisplayName() {
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Descriptor<NomadSlaveTemplate> getDescriptor() {
        return Jenkins.get().getDescriptor(getClass());
    }

    public String createSlaveName() {
        return getPrefix() + "-" + Long.toHexString(System.nanoTime());
    }

    public Set<LabelAtom> getLabelSet() {
        return labelSet;
    }

    public int getNumExecutors() {
        return numExecutors;
    }

    public Node.Mode getMode() {
        return mode;
    }

    public String getPrefix() {
        if(StringUtils.isNotEmpty(prefix))
            return prefix;
        return SLAVE_PREFIX;
    }

    public int getCpu() {
        return cpu;
    }

    public int getMemory() {
        return memory;
    }

    public String getLabels() {
        return labels;
    }

    public List<NomadConstraintTemplate> getConstraints() {
        return Collections.unmodifiableList(constraints);
    }

    public int getIdleTerminationInMinutes() {
        return idleTerminationInMinutes;
    }

    public Boolean getReusable() {
        return reusable;
    }

    public String getRegion() {
        return region;
    }

    public String getDatacenters() {
        return datacenters;
    }

    public int getPriority() {
        return priority;
    }

    public int getDisk() {
        return disk;
    }

    public String getRemoteFs() {
        return remoteFs;
    }

    public Boolean useRawExec() {
        return useRawExec;
    }

    public String getImage() {
        return image;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPrefixCmd() {
        return prefixCmd;
    }

    public String getSwitchUser() {
        return switchUser;
    }

    public String getDriver() {
        return driver;
    }

    public Boolean isDockerDriver(){
        return getDriver().equals("docker");
    }

    public Boolean isJavaDriver(){
        return getDriver().equals("java");
    }

    public Boolean isRawExecDriver(){
        return getDriver().equals("raw_exec");
    }

    public Boolean getPrivileged() {
        return privileged;
    }

    public String getNetwork() {
        return network;
    }

    public Boolean getForcePull() {
        return forcePull;
    }

    public String getSharedMemory() {
        return sharedMem;
    }

    public String getHostVolumes() {
        return hostVolumes;
    }

    public String getHostDevices() {
        return hostDevices;
    }

    public List<? extends NomadPortTemplate> getPorts() {
        if (ports == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(ports);
    }

    public String getCapAdd() {
        return capAdd;
    }

    public String getCapDrop() {
        return capDrop;
    }

    public String getExtraHosts() {
        return extraHosts;
    }
}
