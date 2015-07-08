// -----------------------------------------------------------------------
// <copyright file="ADHelper.cs" company="Microsoft">
// Copyright (c) Microsoft Corporation.  All rights reserved.
// </copyright>
// -----------------------------------------------------------------------

namespace addumper
{
    using System;
    using System.Collections.Generic;
    using System.Text;
    using System.DirectoryServices.ActiveDirectory;
    using System.DirectoryServices;
    using System.Security.Principal;

    /// <summary>
    /// Provides functionalities for accessing AD user/group.
    /// </summary>
    public static class ADHelper
    {
        public static ADPropertyItem[] GetPropertiesByAlias(string alias)
        {
            var p = alias.LastIndexOf('\\');
            if (p >= 0)
                alias = alias.Substring(p + 1);

            using (var entry = SearchAD("sAMAccountName", alias))
            {
                if (entry == null)
                    return null;

                var properties = GetPropertyItems(entry);
                return properties;
            }
        }

        public static ADPropertyItem[] GetPropertiesByID(string alias)
        {
            using (var entry = SearchAD_all("sAMAccountName", alias))
            {
                if (entry == null)
                    return null;

                var properties = GetPropertyItems(entry);
                return properties;
            }
        }

        public static ADPropertyItem[] GetPropertiesByGroup(string groupname)
        {
            using (var entry = SearchAD_all("department", groupname))
            {
                if (entry == null)
                    return null;

                var properties = GetPropertyItems(entry);
                return properties;
            }
        }

        public static ADPropertyItem[] GetPropertiesBySeat(string seat)
        {
            using (var entry = SearchAD_all("physicalDeliveryOfficeName", seat))
            {
                if (entry == null)
                    return null;

                var properties = GetPropertyItems(entry);
                return properties;
            }
        }

        public static ADPropertyItem[] GetPropertiesByPath(string path)
        {
            const string LDAP_PERFIX = "LDAP://";
            if (path.StartsWith(LDAP_PERFIX, StringComparison.OrdinalIgnoreCase))
                path = path.Substring(LDAP_PERFIX.Length);

            using (var entry = SearchAD("distinguishedName", path))
            {
                if (entry == null)
                    return null;

                var properties = GetPropertyItems(entry);
                return properties;
            }
        }

        private static DirectoryEntry SearchAD(string propertyName, string propertyValue)
        {
            var forest = Forest.GetCurrentForest();
            var catalog = forest.FindGlobalCatalog();
            var searcher = catalog.GetDirectorySearcher();
            searcher.SearchScope = SearchScope.Subtree;
            searcher.Filter = string.Format("({0}={1})", propertyName, propertyValue);
            var result = searcher.FindOne();

            DirectoryEntry de = result.GetDirectoryEntry();


            return result == null ? null : result.GetDirectoryEntry();
        }

        private static DirectoryEntry SearchAD_all(string propertyName, string propertyValue)
        {
            HashSet<string> set = new HashSet<string>();


            string[] sl = {"cn",
                            "sn",
                            "title",
                            "physicalDeliveryOfficeName",
                            "telephoneNumber",
                            "givenName",
                            "whenCreated",
                            "displayName",
                            "memberOf",
                            "department",
                            "company",
                            "mailNickname",
                            "name",
                            "sAMAccountName",
                            "managedObjects",
                            "userPrincipalName",
                            "mail",
                            "manager",
                            "mobile"};
            foreach (string f in sl)
            {
                set.Add(f);
            }


            var forest = Forest.GetCurrentForest();
            var catalog = forest.FindGlobalCatalog();
            var searcher = catalog.GetDirectorySearcher();
            searcher.SearchScope = SearchScope.Subtree;
            searcher.Filter = string.Format("({0}={1})", propertyName, propertyValue);
            var result = searcher.FindAll();
            for (int i = 0; i < result.Count; ++i)
            {
                var person = result[i];
                DirectoryEntry de = person.GetDirectoryEntry();
                var list = GetPropertyItems(de);
                string alias = null;
                foreach (ADPropertyItem item in list)
                {
                    if (item.Name == "sAMAccountName")
                    {
                        alias = item.Value;
                        break;
                    }
                }

                if (alias != null)
                {
                    foreach (ADPropertyItem item in list)
                    {
                        if (set.Contains(item.Name)) 
                        {
                            string value = item.Value.ToString();
                            value = value.Replace("\r\n", " ");
                            value = value.Replace('\n', ' ');
                            Console.WriteLine("{0}\t{1}\t{2}", alias, item.Name, value);
                        }
                    }
                }
            }
            return null;

            //DirectoryEntry de = result.GetDirectoryEntry();


            //return result == null ? null : result.GetDirectoryEntry();
        }

        private static ADPropertyItem[] GetPropertyItems(DirectoryEntry entry)
        {
            var properties = new List<ADPropertyItem>(entry.Properties.Count);

            foreach (string pn in entry.Properties.PropertyNames)
            {
                var pv = entry.Properties[pn];
                var pi = CreatePropertyItem(pn, pv);
                properties.Add(pi);
            }

            return properties.ToArray();
        }

        private static ADPropertyItem CreatePropertyItem(string name, PropertyValueCollection value)
        {
            if (value == null)
            {
                return new ADPropertyItem
                {
                    Name = name,
                    Type = "---- NULL ----",
                    Value = null,
                };
            }
            else
            {
                var values = new string[value.Count];

                for (var i = 0; i < values.Length; i++)
                {
                    values[i] = FormatValue(name, value[i]);
                }

                var valString = string.Join("\r\n", values);
                var type = value.Count > 0 ? value[0].GetType().ToString() : "---- EMPTY ----";
                return new ADPropertyItem
                {
                    Name = name,
                    Type = type,
                    Value = valString,
                };
            }
        }

        private static string FormatValue(string name, object value)
        {
            if (name == "objectSid" && value is byte[])
            {
                var si = new SecurityIdentifier((byte[])value, 0);
                return si.Value;
            }
            else if (name.EndsWith("guid", StringComparison.OrdinalIgnoreCase) && value is byte[])
            {
                var guid = new Guid((byte[])value);
                return guid.ToString();
            }
            else if (value is byte[])
            {
                var str = BitConverter.ToString((byte[])value);
                return str;
            }
            else
            {
                return value.ToString();
            }
        }
    }
}
