using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace gorzService
{
    public class GorzService
    {
        private static double PI = 3.14159265;
        public string connectionString;

        public GorzService(string connectionString)
        {
            this.connectionString = connectionString;
        }
        public List<topicModel> LoadTopics()
        {
            List<topicModel> allTopics = new List<topicModel>();
            DataTable dataTable = new DataTable();
            dataTable.TableName = "Topic";


            String sql = @"select * from dbo.Topic order by up desc";
            SqlCommand cmd = new SqlCommand
            {
                Connection = new SqlConnection(connectionString),
                CommandText = sql,
                CommandType = CommandType.Text
            };
            cmd.CommandTimeout = 0;

            SqlDataAdapter da = new SqlDataAdapter(cmd);
            try
            {
                da.Fill(dataTable);
                if (dataTable != null)
                {
                    allTopics = (from DataRow row in dataTable.Rows

                                 select new topicModel
                                 {
                                     id = Convert.ToInt32(row["id"]),
                                     topicName = row["topicTitle"].ToString(),
                                     stime = Convert.ToDateTime(row["stime"]),
                                     etime = Convert.ToDateTime(row["etime"]),
                                     longitude = Convert.ToDouble(row["longitude"]),
                                     latitude = Convert.ToDouble(row["latitude"]),
                                     locationAddr = row["locationAddr"].ToString(),
                                     owner = row["owner"].ToString(),
                                     up = Convert.ToInt32(row["up"]),
                                     desc = row["topicDescription"].ToString()
                                 }).ToList();

                }
            }
            catch (Exception ex)
            {
                throw ex;
            }
            finally
            {
                if (cmd.Connection != null && cmd.Connection.State.ToString() != "Close")
                {
                    cmd.Connection.Close();
                    cmd.Connection.Dispose();

                }
            }

            return allTopics;

        }

        /**
     * @param raidus 单位米
     * return minLat,minLng,maxLat,maxLng
     */
        public double[] getAround(double lat, double lon, double raidus)
        {

            Double latitude = lat;
            Double longitude = lon;

            Double degree = (24901 * 1609) / 360.0;
            double raidusMile = raidus;

            Double dpmLat = 1 / degree;
            Double radiusLat = dpmLat * raidusMile;
            Double minLat = latitude - radiusLat;
            Double maxLat = latitude + radiusLat;

            Double mpdLng = degree * Math.Cos(latitude * (PI / 180));
            Double dpmLng = 1 / mpdLng;
            Double radiusLng = dpmLng * raidusMile;
            Double minLng = longitude - radiusLng;
            Double maxLng = longitude + radiusLng;
            return new double[] { minLat, minLng, maxLat, maxLng };
        }

        public List<topicModel> search(string title, double latitude, double longitude, DateTime stime, DateTime etime, double distance)
        {
            List<topicModel> allTopics = new List<topicModel>();
            if (distance != 0)
            {
                DataTable dataTable = new DataTable();
                dataTable.TableName = "Topic";

                String sql = @"select * from dbo.Topic where";


                if (stime != null)
                {
                    sql += " stime >='" + stime + "'";
                }
                if (etime != null)
                {
                    sql += " and etime <='" + etime + "'";
                }
                if (title != null)
                {
                    sql += " and topicTitle like '%" + title + "%' ";
                }
                if (latitude != 0 || longitude != 0)
                {
                    double[] result = getAround(latitude, longitude, distance * 1000);
                    
                    if (result[2] < result[0])
                    {
                        double tmp = result[0];
                        result[0] = result[2];
                        result[2] = tmp;
                    }

                    if (result[1] < result[3])
                    {
                        double tmp = result[1];
                        result[1] = result[3];
                        result[3] = tmp;
                    }

                    sql += " and latitude between " + result[0] + " and " + result[2] + " and longitude between " + result[3] + " and " + result[1];
                }


                SqlCommand cmd = new SqlCommand
                {
                    Connection = new SqlConnection(connectionString),
                    CommandText = sql,
                    CommandType = CommandType.Text
                };

                SqlDataAdapter da = new SqlDataAdapter(cmd);
                try
                {
                    da.Fill(dataTable);
                    if (dataTable != null)
                    {
                        allTopics = ConvertFromDataTable(dataTable);
                    }
                }
                catch (Exception ex)
                {
                    throw ex;
                }
                finally
                {
                    if (cmd.Connection != null && cmd.Connection.State.ToString() != "Close")
                    {
                        cmd.Connection.Close();
                        cmd.Connection.Dispose();

                    }
                }

            }
            else
            {
                DataTable dataTable = new DataTable();
                dataTable.TableName = "Topic";

                String sql = "";
                if (latitude != 0 || longitude != 0)
                {
                    sql = @"select * from dbo.Topic where latitude = " + latitude + " and longitude = " + longitude;
                }
                else
                {
                    sql = @"select * from dbo.Topic where 1=1";
                }

                if (title != null)
                {
                    sql += " and topicTitle like '%" + title + "%'";
                }
                if (stime != null)
                {
                    sql += " and stime >='" + stime + "'";
                }
                if (etime != null)
                {
                    sql += " and etime <='" + etime + "'";
                }

                SqlCommand cmd = new SqlCommand
                {
                    Connection = new SqlConnection(connectionString),
                    CommandText = sql,
                    CommandType = CommandType.Text
                };

                SqlDataAdapter da = new SqlDataAdapter(cmd);
                try
                {
                    da.Fill(dataTable);
                    if (dataTable != null)
                    {
                        allTopics = ConvertFromDataTable(dataTable);
                    }
                }
                catch (Exception ex)
                {
                    throw ex;
                }
                finally
                {
                    if (cmd.Connection != null && cmd.Connection.State.ToString() != "Close")
                    {
                        cmd.Connection.Close();
                        cmd.Connection.Dispose();

                    }
                }

            }
            return allTopics;
        }

        private List<topicModel> ConvertFromDataTable(DataTable dataTable)
        {
            return (from DataRow row in dataTable.Rows

                    select new topicModel
                    {
                        id = Convert.ToInt32(row["id"]),
                        topicName = row["topicTitle"].ToString(),
                        stime = Convert.ToDateTime(row["stime"]),
                        etime = Convert.ToDateTime(row["etime"]),
                        longitude = Convert.ToDouble(row["longitude"]),
                        latitude = Convert.ToDouble(row["latitude"]),
                        locationAddr = row["locationAddr"].ToString(),
                        owner = row["owner"].ToString(),
                        up = Convert.ToInt32(row["up"]),
                        desc = row["topicDescription"].ToString()
                    }).ToList();
        }

        public int save(topicModel newRecord)
        {
            SqlCommand cmd = new SqlCommand();
            int successNum = 0;
            try
            {
                cmd.CommandType = CommandType.Text;
                cmd.Connection = new SqlConnection(connectionString);
                string sqlIns = "INSERT INTO topic (topicTitle, stime,etime, longitude,latitude,locationAddr, owner,up,topicDescription) VALUES (@name, @stime,@etime, @longitude,@latitude,@locationAddr, @owner,@up,@desc)";
                cmd.CommandText = sqlIns;
                if (newRecord.topicName == null)
                    newRecord.topicName = "";
                cmd.Parameters.AddWithValue("@name", newRecord.topicName);

                cmd.Parameters.AddWithValue("@stime", newRecord.stime);
                cmd.Parameters.AddWithValue("@etime", newRecord.etime);

                cmd.Parameters.AddWithValue("@longitude", newRecord.longitude);
                cmd.Parameters.AddWithValue("@latitude", newRecord.latitude);
                cmd.Parameters.AddWithValue("@locationAddr", newRecord.locationAddr==null? "China" : newRecord.locationAddr);
                if (newRecord.owner == null)
                    newRecord.owner = "";
                cmd.Parameters.AddWithValue("@owner", newRecord.owner);
                cmd.Parameters.AddWithValue("@up", newRecord.up);
                if (newRecord.desc == null)
                    newRecord.desc = "";
                cmd.Parameters.AddWithValue("@desc", newRecord.desc);

                cmd.Connection.Open();
                successNum = cmd.ExecuteNonQuery();
                cmd.Dispose();

            }
            catch (Exception ex)
            {
                throw new Exception(ex.ToString(), ex);
            }
            finally
            {
                if (cmd.Connection != null && cmd.Connection.State.ToString() != "Close")
                {
                    cmd.Connection.Close();
                    cmd.Connection.Dispose();

                }
            }

            return successNum;
        }
        public int update(topicModel newRecord)
        {
            SqlCommand cmd = new SqlCommand();
            int successNum = 0;
            try
            {

                cmd.CommandType = CommandType.Text;
                cmd.Connection = new SqlConnection(connectionString);
                string sql = "update  topic set topicTitle=@name, longitude=@longitude,latitude=@latitude,locationAddr=@locationAddr, owner=@owner,up=@up,topicDescription=@desc,stime=@stime,etime=@etime where id=@id";

                cmd.CommandText = sql;
                if (newRecord.topicName == null)
                    newRecord.topicName = "";
                if (newRecord.owner == null)
                    newRecord.owner = "";
                if (newRecord.desc == null)
                    newRecord.desc = "";
                cmd.Parameters.AddWithValue("@name", newRecord.topicName);

                cmd.Parameters.AddWithValue("@stime", newRecord.stime);
                cmd.Parameters.AddWithValue("@etime", newRecord.etime);
                cmd.Parameters.AddWithValue("@desc", newRecord.desc);

                cmd.Parameters.AddWithValue("@longitude", newRecord.longitude);
                cmd.Parameters.AddWithValue("@latitude", newRecord.latitude);
                cmd.Parameters.AddWithValue("@locationAddr", newRecord.locationAddr);
                cmd.Parameters.AddWithValue("@owner", newRecord.owner);
                cmd.Parameters.AddWithValue("@up", newRecord.up);
                cmd.Parameters.AddWithValue("@id", newRecord.id);

                cmd.Connection.Open();
                successNum = cmd.ExecuteNonQuery();
                cmd.Dispose();

            }
            catch (Exception ex)
            {
                throw new Exception(ex.ToString(), ex);
            }
            finally
            {
                if (cmd.Connection != null && cmd.Connection.State.ToString() != "Close")
                {
                    cmd.Connection.Close();
                    cmd.Connection.Dispose();

                }
            }

            return successNum;
        }
    }
}
