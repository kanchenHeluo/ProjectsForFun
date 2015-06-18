using System;
using System.Web.Mvc;
using Newtonsoft.Json;

namespace MachAroundU.Common
{
    /// <summary>
    /// Json.NET wrapper for JsonResult
    /// </summary>
    public class JsonNetResult : JsonResult
    {
        public override void ExecuteResult(ControllerContext context)
        {
            if (context == null)
            {
                throw new ArgumentNullException("context");
            }

            var response = context.HttpContext.Response;

            response.ContentType = !String.IsNullOrEmpty(ContentType) ? ContentType : "application/json";

            if (ContentEncoding != null)
            {
                response.ContentEncoding = ContentEncoding;
            }

            if (Data == null)
            {
                return;
            }

            var settings = new JsonSerializerSettings()
            {
                NullValueHandling = NullValueHandling.Ignore
            };

#if DEBUG
            var json = JsonConvert.SerializeObject(Data, settings);
#endif
            var ser = JsonSerializer.Create(settings);

            ser.Serialize(response.Output, Data);
        }
    }
}