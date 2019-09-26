import org.omg.CORBA.ORB;
import org.omg.CosNaming.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


class Scratch {
    public static void main(String[] args) {

        //Initialize ORB
        ORB orbRef = org.omg.CORBA.ORB.init(args, null);
        File f = new File("ns.ior");

        if (!f.exists())
        {
            System.out.println("File is not valid");
        } else if (f.isDirectory())
        {
            System.out.println("Path is directory");
        }

        try {
            FileReader temp = new FileReader(f);
            BufferedReader br = new BufferedReader(temp);
            String iorContent = br.readLine(); // Read content of ior file
            org.omg.CORBA.Object obj = orbRef.string_to_object(iorContent);

            NamingContext nmContext = NamingContextHelper.narrow(obj);
            NamingContext nmContextVaries = nmContext;

            NameComponent root = new NameComponent("TMF_MTNM","Class");
            NameComponent vendor = new NameComponent("HUAWEI","Vendor");
            NameComponent emsInstance = new NameComponent("Huawei/U2000","EmsInstance");
            NameComponent version = new NameComponent("2.0","Version");
            NameComponent factory = new NameComponent("Huawei/U2000","EmsSessionFactory_I");
            //NameComponent factoryPath[] = {vendor, emsInstance, version, factory};
            NameComponent factoryPath[] = {root, vendor, emsInstance, version};
            org.omg.CORBA.Object factoryRef = nmContext.resolve(factoryPath);

            NamingContext factoryNamingContextRef = NamingContextHelper.narrow(factoryRef);


            BindingListHolder bl = new BindingListHolder();
            BindingIteratorHolder blIt = new BindingIteratorHolder();
            factoryNamingContextRef.list(1000,bl,blIt);
            Binding binding[] = bl.value;
            if (binding.length == 0) return;

            for (int i = 0; i < binding.length; i++)
            {
                org.omg.CORBA.Object objRef = factoryNamingContextRef.resolve(binding[i].binding_name);
                String objString = objRef.toString();
                int lastIx = binding[i].binding_name.length-1;

                if (binding[i].binding_type == BindingType.ncontext) {

                    System.out.println("Context: " + binding[i].binding_name[lastIx].id);
                } else {
                    System.out.println("Objet: " + binding[i].binding_name[lastIx].id);
                }
            }

        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("Error");
        }





    }
}