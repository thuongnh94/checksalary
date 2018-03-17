package haui.vn.checksalary;

public class Staff {
    public String email = "", name = "", chuc_vu = "";
    public long luongcb = 0;
    public long phu_cap = 0;
    public long di_lai = 0;
    public long nha_o = 0;
    public long trach_nhiem;
    public long bhyt = 0;
    public long bhxh = 0;
    public long bhtn = 0;

    public Staff() {
    }

    public Staff(String email, String name, String chuc_vu, long luongcb, long phu_cap, long di_lai, long nha_o, long trach_nhiem) {
        this.email = email;
        this.name = name;
        this.chuc_vu = chuc_vu;
        this.luongcb = luongcb;
        this.phu_cap = phu_cap;
        this.di_lai = di_lai;
        this.nha_o = nha_o;
        this.trach_nhiem = trach_nhiem;
        this.bhyt = luongcb / 100;
        this.bhxh = luongcb * 8 / 100;
        this.bhtn = luongcb / 100;
    }
}
