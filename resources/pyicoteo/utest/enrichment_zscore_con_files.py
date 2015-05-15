    #################################
    #       DEPRECATED?             #   
    #################################

    def __load_enrichment_result_chungo(self, lines):
        ret = []
        for line in lines:
            sline = line.split()
            try:
                float(sline[1])
                ret.append(dict(zip(self.enrichment_keys, sline)))
            except ValueError:
                pass

        return ret  

    def calculate_zscore_chungo(self, values_path):
        num_regions = sum(1 for line in open(values_path))
        bin_size = int(self.binsize*num_regions)
        if bin_size < 50:
            self.logger.warning("The bin size results in a sliding window smaller than 50, adjusting window to 50 in order to get statistically meaningful results.")
            bin_size = 50

        bin_step = max(1, int(self.bin_step*num_regions))
        self.logger.info("Enrichment window calculation using a sliding window size of %s, sliding with a step of %s"%(bin_size, bin_step))
        self.logger.info("... calculating zscore...")

        sorter = utils.BigSort('bed', False, 0, 'zscore_sorter%s'%os.getpid(), logger=self.logger, filter_chunks=False)

        aprime_sorted = sorter.sort(values_path, None, lambda line:(float(line.split()[16]))) #sort by A prime

        old_file_path = '%s/before_zscore_%s'%(self._current_directory(), os.path.basename(values_path)) #create path for the outdated file
        shutil.move(os.path.abspath(values_path), old_file_path) #move the file
        new_file = open(values_path, 'w') #open a new file in the now empty file space

        result_chunk = []
        self.points = []
        #get the standard deviations range(0, num_regions-bin_size+bin_step, bin_step)

        for line in aprime_sorted:
            result_chunk.append(dict(zip(self.enrichment_keys, line.split()))) #add the line to the result_chunk
            if len(result_chunk) > bin_size: # if the result chunk is smaller than the bin_size, remove oldest item
                entry = result_chunk.pop(0)
                new_file.write("\t".join(str(entry[key]) for key in self.enrichment_keys)+"\n")

            if len(result_chunk) == bin_size: #we have the size of the bin: operate
                #retrieve the values
                mean_acum = 0
                a_acum = 0
                Ms_replica = []
                for entry in result_chunk:
                    mean_acum += float(entry["M_prime"])
                    a_acum += float(entry["A_prime"])
                    Ms_replica.append(float(entry["M_prime"]))

                #add them to the points of mean and sd
                mean = mean_acum/len(result_chunk)
                sd = (sum((x - mean)**2 for x in Ms_replica))/len(Ms_replica) 
                A_median = a_acum / len(result_chunk)
                self.points.append([A_median, mean, sd]) #The A asigned to the window, the mean and the standard deviation  
                self.logger.debug("Window #%s: length: %s A median: %s mean: %s sd: %s"%(len(self.points), len(result_chunk), self.points[-1][0], self.points[-1][1], self.points[-1][2]))
                #update z scores
                for entry in result_chunk:
                    entry["A_median"] = 0
                    entry["mean"] = 0
                    entry["sd"] = 0
                    entry["zscore"] = 0
                    closest_A = sys.maxint
                    sd_position = 0

                    for i in range(0, len(self.points)):
                        new_A = self.points[i][0]
                        if new_A != closest_A: #skip repeated points
                            if abs(closest_A - float(entry["A"])) >= abs(new_A - float(entry["A"])):
                                closest_A = new_A
                                sd_position = i
                            else:
                                break #already found, no need to go further since the points are ordered
                                
                    entry["A_median"] = closest_A
                    self.__sub_zscore(entry, self.points[sd_position]) #the value is the biggest, use the last break
                    
        for entry in result_chunk: # the last entries
            new_file.write("\t".join(str(entry[key]) for key in self.enrichment_keys)+"\n")

        new_file.flush()
        self._manage_temp_file(aprime_sorted.name)
        self._manage_temp_file(old_file_path)
        result_file = sorter.sort(new_file.name, None, lambda x:(x.split()[0], int(x.split()[1]), int(x.split()[2]))) #sort by chr, start, end

        return result_file.name

    #################################
    #     END  DEPRECATED?          #   
    #################################

