package yukon.service;

/**
 * Created by vitalii on 01/02/2017.
 */
public class ServiceDTO {
    private String address = "localhost";
    private int port;

    public ServiceDTO() {
    }

    public ServiceDTO(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServiceDTO that = (ServiceDTO) o;

        if (port != that.port) {
            return false;
        }
        return address != null ? address.equals(that.address) : that.address == null;
    }

    @Override
    public int hashCode() {
        int result = address != null ? address.hashCode() : 0;
        result = 31 * result + port;
        return result;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" ")
                .append(address)
                .append(":")
                .append(port)
                .append(" ");
        return sb.toString();
    }
}
